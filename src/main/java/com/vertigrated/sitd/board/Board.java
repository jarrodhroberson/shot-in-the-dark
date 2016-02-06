package com.vertigrated.sitd.board;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.vertigrated.fluent.Build;
import com.vertigrated.fluent.Dimension;
import com.vertigrated.fluent.Targets;
import com.vertigrated.pattern.Strategy;
import com.vertigrated.sitd.Coordinate;
import com.vertigrated.sitd.Coordinates;
import com.vertigrated.sitd.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkNotNull;

@JsonSerialize(using = Board.Serializer.class)
@JsonDeserialize(using = Board.Deserializer.class)
public class Board
{
    private static final Logger L = LoggerFactory.getLogger(Board.class);

    public final UUID id;
    public final Integer width;
    public final Integer height;
    public final Set<Target> targets;

    Board(@Nonnull final Integer dimension, @Nonnull final Set<Target> targets)
    {
        this(dimension, dimension, targets);
    }

    Board(@Nonnull final Integer width, @Nonnull final Integer height, @Nonnull final Set<Target> targets)
    {
        this.width = width;
        this.height = height;
        this.targets = Sets.newTreeSet();
        for (final Target t : targets)
        {
            this.place(t);
        }
        this.id = UUID.nameUUIDFromBytes(this.toString().getBytes(UTF_8));
    }

    final boolean place(@Nonnull final Target target)
    {
        final Coordinate end = target.coordinates.end();
        if (end.x >= this.width || end.y >= this.height) { return false; }
        for (final Target t : this.targets) { if (target.intersects(t)) { return false; } }
        this.targets.add(target);
        return true;
    }

    Set<Coordinates> taken()
    {
        return ImmutableSortedSet.copyOf(Iterables.transform(this.targets, new Function<Target, Coordinates>()
        {
            @Nullable
            @Override
            public Coordinates apply(@Nullable final Target input)
            {
                return checkNotNull(input).coordinates;
            }
        }));
    }

    @Nullable
    public Target at(@Nonnull final Integer x, @Nonnull final Integer y)
    {
        final Coordinate key = new Coordinate(x, y);
        return Iterables.tryFind(this.targets, new Predicate<Target>()
        {
            @Override
            public boolean apply(@Nullable final Target target)
            {
                return checkNotNull(target).contains(key);
            }
        }).orNull();
    }

    public boolean test(@Nonnull final Integer x, @Nonnull final Integer y)
    {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) { throw new IllegalArgumentException(); }
        else
        {
            final Coordinate c = new Coordinate(x, y);
            for (final Target t : this.targets)
            {
                if (t.contains(c)) { return true; }
            }
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id, width, height, targets);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final Board board = (Board) o;
        return this.toString().equals(board.toString());
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("id", id)
                          .add("width", width)
                          .add("height", height)
                          .add("targets", targets)
                          .toString();
    }

    public static class Builder implements Dimension<Targets<Build<com.vertigrated.sitd.board.Board>>, Integer>
    {
        @Nonnull
        @Override
        public Targets<Build<Board>> dimension(@Nonnull final Integer width, @Nonnull final Integer height)
        {
            return new Targets<Build<Board>>()
            {
                @Nonnull
                @Override
                public Build<Board> targets(@Nonnull final Set<Target> targets)
                {
                    return new Build<Board>()
                    {
                        @Override
                        public Board build()
                        {
                            return new Board(width, height, targets);
                        }
                    };
                }

                @Nonnull
                @Override
                public Build<Board> targets(@Nonnull final Strategy<Board, Set<Target>> strategy)
                {
                    return this.targets(strategy.apply(new Board(width, height, Sets.<Target>newTreeSet())));
                }
            };
        }

        @Nonnull
        @Override
        public Targets<Build<Board>> dimension(@Nonnull final Integer side)
        {
            return this.dimension(side, side);
        }
    }

    public static class Serializer extends JsonSerializer<Board>
    {
        @Override
        public void serialize(final Board value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException, JsonProcessingException
        {
            gen.writeStartObject();
            gen.writeObjectField("id", value.id);
            gen.writeNumberField("width", value.width);
            gen.writeNumberField("height", value.height);
            gen.writeArrayFieldStart("targets");
            for (final Target t : value.targets) { gen.writeObject(t); }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }

    public static class Deserializer extends JsonDeserializer<Board>
    {
        @Nonnull
        @Override
        public Board deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException
        {
            final JsonNode n = p.readValueAsTree();
            return new Builder().dimension(n.get("width").asInt(), n.get("height").asInt())
                                .targets(new Function<JsonNode, Set<Target>>()
                                {
                                    @Nonnull
                                    @Override
                                    public Set<Target> apply(@Nullable final JsonNode input)
                                    {
                                        final ImmutableSortedSet.Builder<Target> issb = ImmutableSortedSet.naturalOrder();
                                        if (checkNotNull(input).isArray())
                                        {
                                            for (final JsonNode jn : input)
                                            {
                                                try { issb.add(p.getCodec().treeToValue(jn, Target.class)); }
                                                catch (JsonProcessingException e) { throw new RuntimeException(e); }

                                            }
                                        }
                                        return issb.build();
                                    }
                                }.apply(n.get("targets"))).build();
        }
    }

}
