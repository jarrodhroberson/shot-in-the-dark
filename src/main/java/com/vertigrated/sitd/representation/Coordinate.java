package com.vertigrated.sitd.representation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;
import com.google.common.primitives.Ints;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

@JsonSerialize(using = Coordinate.Serializer.class)
@JsonDeserialize(using = Coordinate.Deserializer.class)
public class Coordinate implements Comparable<Coordinate>
{

    public final Integer x;
    public final Integer y;

    public Coordinate(@Nonnull final Integer x, @Nonnull final Integer y)
    {
        this.x = x;
        this.y = y;
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

    @Override public int compareTo(@Nonnull final Coordinate o)
    {
        if (this.y.equals(o.y)) { return Ints.compare(this.x,o.x); }
        else if (this.x.equals(o.x)) { return Ints.compare(this.y,o.y); }
        else { return Ints.compare(this.y,o.y); }
    }

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
        @Nonnull
        @Override public Coordinate deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException
        {
            final JsonNode n = p.readValueAsTree();
            return new Coordinate(n.get("x").asInt(), n.get("y").asInt());
        }
    }

    static class VerticalDiscreteDomain extends DiscreteDomain<Coordinate>
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
            if (value.y.equals(minValue().y)) { return null; }
            else {return new Coordinate(value.x, value.y - 1);}
        }

        @Override public long distance(@Nonnull final Coordinate start, @Nonnull final Coordinate end) { return end.y - start.y; }

        @Nonnull
        @Override
        public Coordinate minValue() { return coordinateRange.lowerEndpoint(); }

        @Nonnull
        @Override
        public Coordinate maxValue() { return coordinateRange.upperEndpoint(); }
    }

    static class HorizontalDiscreteDomain extends DiscreteDomain<Coordinate>
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
            if (value.x.equals(minValue().x)) { return null; }
            else {return new Coordinate(value.x + 1, value.y);}
        }

        @Override public long distance(@Nonnull final Coordinate start, @Nonnull final Coordinate end) { return end.x - start.x; }

        @Nonnull
        @Override
        public Coordinate minValue() { return coordinateRange.lowerEndpoint(); }

        @Nonnull
        @Override
        public Coordinate maxValue() { return coordinateRange.upperEndpoint(); }
    }
}
