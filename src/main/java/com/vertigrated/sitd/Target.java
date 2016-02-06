package com.vertigrated.sitd;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Longs;
import com.google.inject.Inject;
import com.vertigrated.converter.JsonNodeToObject;
import com.vertigrated.fluent.Build;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

@JsonSerialize(using = Target.Serializer.class)
@JsonDeserialize(using = Target.Deserializer.class)
public class Target implements Comparable<Target>
{
    public static final Ordering<Target> SIZE = new Ordering<Target>()
    {
        @Override
        public int compare(@Nullable final Target left, @Nullable final Target right)
        {
            return Longs.compare(checkNotNull(left).size(), checkNotNull(right).size());
        }
    };

    public static final Ordering<Target> COORDINATES = new Ordering<Target>()
    {
        @Override
        public int compare(@Nullable final Target left, @Nullable final Target right)
        {
            return checkNotNull(left).coordinates.compareTo(checkNotNull(right).coordinates);
        }
    };

    public final Coordinates coordinates;

    public Target(@Nonnull final Coordinates coordinates)
    {
        this.coordinates = coordinates;
    }

    public Orientation orientation() { return this.coordinates.orientation(); }

    public boolean intersects(@Nonnull final Target target)
    {
        final Set<Coordinate> coordinates = this.coordinates.asSet();
        for (final Coordinate c : target.coordinates.asSet())
        {
            if (coordinates.contains(c)) { return true; }
        }
        return false;
    }

    public boolean contains(@Nonnull final Coordinate coordinate)
    {
        return Iterables.tryFind(this.coordinates.asSet(), new Predicate<Coordinate>()
        {
            @Override
            public boolean apply(@Nullable final Coordinate input)
            {
                return coordinate.x.equals(checkNotNull(input).x) && coordinate.y.equals(input.y);
            }
        }).isPresent();
    }

    public long size() { return this.coordinates.size(); }

    @Override
    public int compareTo(@Nonnull final Target o)
    {
        return SIZE.compound(COORDINATES).compare(this, o);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(coordinates);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final Target target = (Target) o;
        return Objects.equal(coordinates, target.coordinates);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("coordinates", coordinates)
                          .toString();
    }

    public static class Builder implements com.vertigrated.fluent.Coordinates<Build<Target>>
    {
        @Nonnull
        @Override
        public Build<Target> coordinates(@Nonnull final Coordinates coordinates)
        {
            return new Build<Target>() {
                @Nonnull
                @Override
                public Target build()
                {
                    return new Target(coordinates);
                }
            };
        }
    }

    public static class Serializer extends JsonSerializer<Target>
    {

        @Override
        public void serialize(final Target value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException, JsonProcessingException
        {
            gen.writeStartObject();
            gen.writeObjectField("coordinates", value.coordinates);
            gen.writeEndObject();
        }
    }

    public static class Deserializer extends JsonDeserializer<Target>
    {
        @Nonnull
        private final JsonNodeToObject<Coordinates> converter;

        @Inject
        Deserializer(@Nonnull final JsonNodeToObject<Coordinates> converter)
        {
            this.converter = converter;
        }

        @Nonnull
        @Override
        public Target deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException
        {
            final JsonNode n = p.readValueAsTree();
            return new Builder().coordinates(this.converter.convert(n.get("coordinates")))
                                .build();
        }
    }
}
