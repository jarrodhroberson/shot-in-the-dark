package com.vertigrated.sitd;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Ordering;
import com.google.common.collect.Range;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.vertigrated.sitd.Orientation.HORIZONTAL;
import static com.vertigrated.sitd.Orientation.VERTICAL;
import static java.lang.String.format;

@JsonSerialize(using = Coordinate.Serializer.class)
@JsonDeserialize(using = Coordinate.Deserializer.class)
public class Coordinate implements Comparable<Coordinate>
{
    public static Ordering<Coordinate> NATURAL_X = new Ordering<Coordinate>()
    {
        @Override public int compare(@Nullable final Coordinate coordinate, @Nullable final Coordinate t1)
        {
            return checkNotNull(coordinate).x.compareTo(checkNotNull(t1).x);
        }
    };
    public static Ordering<Coordinate> NATURAL_Y = new Ordering<Coordinate>()
    {
        @Override public int compare(@Nullable final Coordinate coordinate, @Nullable final Coordinate t1)
        {
            return checkNotNull(coordinate).y.compareTo(checkNotNull(t1).y);
        }
    };
    public static Ordering<Coordinate> NATURAL = NATURAL_X.compound(NATURAL_Y);

    public static Set<Coordinate> coordinates(@Nonnull final Range<Coordinate> coordinateRange)
    {
        return ContiguousSet.create(coordinateRange, from(coordinateRange).equals(HORIZONTAL) ? new HorizontalDiscreteDomain(coordinateRange) : new VerticalDiscreteDomain(coordinateRange));
    }

    public static Orientation from(@Nonnull final Range<Coordinate> coordinateRange)
    {
        if (coordinateRange.lowerEndpoint().x.equals(coordinateRange.upperEndpoint().x))
        {
            return HORIZONTAL;
        }
        else if (coordinateRange.lowerEndpoint().y.equals(coordinateRange.upperEndpoint().y))
        {
            return VERTICAL;
        }
        else
        {
            throw new IllegalArgumentException(format("%s and %s do not represent a Horizontal or Vertical contigous range!", coordinateRange.lowerEndpoint(), coordinateRange.upperEndpoint()));
        }
    }

    public final Integer x;
    public final Integer y;

    public Coordinate(@Nonnull final Integer x, @Nonnull final Integer y)
    {
        this.x = x;
        this.y = y;
    }

    public Orientation orientation(@Nonnull final Coordinate start, @Nonnull final Coordinate end)
    {
        return start.x.equals(end.x) ? HORIZONTAL : VERTICAL;
    }

    @Override public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final Coordinate that = (Coordinate) o;
        return Objects.equal(x, that.x) &&
               Objects.equal(y, that.y);
    }

    @Override public int hashCode() { return Objects.hashCode(x, y); }

    @Override public int compareTo(final Coordinate o) { return NATURAL.compare(this, o); }

    @Override public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("x", x)
                          .add("y", y)
                          .toString();
    }

    static class Serializer extends JsonSerializer<Coordinate>
    {
        @Override public void serialize(final Coordinate value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException, JsonProcessingException
        {
            gen.writeStartObject();
            gen.writeNumberField("x", value.x);
            gen.writeNumberField("y", value.y);
            gen.writeEndObject();
        }
    }

    static class Deserializer extends JsonDeserializer<Coordinate>
    {
        @Override public Coordinate deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException
        {
            final JsonNode n = p.readValueAsTree();
            return new Coordinate(n.get("x").asInt(), n.get("y").asInt());
        }
    }

    public static class HorizontalDiscreteDomain extends DiscreteDomain<Coordinate>
    {
        private final Range<Coordinate> coordinateRange;

        public HorizontalDiscreteDomain(@Nonnull final Range<Coordinate> coordinateRange)
        {
            this.coordinateRange = coordinateRange;
        }

        @Nullable
        @Override
        public Coordinate next(@Nonnull final Coordinate value)
        {
            if (value.x.equals(maxValue().x)) { return null; }
            else {return new Coordinate(value.x + 1, value.y);}
        }

        @Nullable
        @Override
        public Coordinate previous(@Nonnull final Coordinate value)
        {
            if (value.y.equals(minValue().y)) { return null; }
            else {return new Coordinate(value.x, value.y - 1);}
        }

        @Override public long distance(@Nonnull final Coordinate start, @Nonnull final Coordinate end) { return end.x - start.x; }

        @Nonnull
        @Override
        public Coordinate minValue() { return coordinateRange.lowerEndpoint(); }

        @Nonnull
        @Override
        public Coordinate maxValue() { return coordinateRange.upperEndpoint(); }
    }

    public static class VerticalDiscreteDomain extends DiscreteDomain<Coordinate>
    {
        private final Range<Coordinate> coordinateRange;

        public VerticalDiscreteDomain(@Nonnull final Range<Coordinate> coordinateRange)
        {
            this.coordinateRange = coordinateRange;
        }

        @Nullable
        @Override
        public Coordinate next(@Nonnull final Coordinate value)
        {
            if (value.y.equals(maxValue().y)) { return null; }
            else {return new Coordinate(value.x, value.y + 1);}
        }

        @Nullable
        @Override
        public Coordinate previous(@Nonnull final Coordinate value)
        {
            if (value.x.equals(minValue().x)) { return null; }
            else {return new Coordinate(value.x + 1, value.y);}
        }

        @Override public long distance(@Nonnull final Coordinate start, @Nonnull final Coordinate end) { return end.y - start.y; }

        @Nonnull
        @Override
        public Coordinate minValue() { return coordinateRange.lowerEndpoint(); }

        @Nonnull
        @Override
        public Coordinate maxValue() { return coordinateRange.upperEndpoint(); }
    }
}
