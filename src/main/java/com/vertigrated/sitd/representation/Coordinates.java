package com.vertigrated.sitd.representation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.guava.deser.RangeDeserializer;
import com.fasterxml.jackson.datatype.guava.ser.RangeSerializer;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Ordering;
import com.google.common.collect.Range;
import com.vertigrated.fluent.Build;
import com.vertigrated.fluent.End;
import com.vertigrated.fluent.Start;
import com.vertigrated.sitd.representation.Coordinate.HorizontalDiscreteDomain;
import com.vertigrated.sitd.representation.Coordinate.VerticalDiscreteDomain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

@JsonSerialize(using = Coordinates.Serializer.class)
@JsonDeserialize(using = Coordinates.Deserializer.class)
public class Coordinates implements Iterable<Coordinate>, Comparable<Coordinates>
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
    public static Ordering<Coordinate> VERTICAL = NATURAL_Y.compound(NATURAL_X);
    public static Ordering<Coordinate> HORIZONTAL = NATURAL_X.compound(NATURAL_Y);

    private static Orientation of(@Nonnull final Coordinate left, @Nonnull final Coordinate right)
    {
        if (left.x.equals(right.x)) { return Orientation.VERTICAL; }
        else if (left.y.equals(right.y)) { return Orientation.HORIZONTAL; }
        else { throw new IllegalArgumentException(format("%s and %s do not represent a Horizontal or Vertical contigous range!", left, right)); }
    }

    @JsonSerialize(using = RangeSerializer.class)
    @JsonDeserialize(using = RangeDeserializer.class)
    private final Range<Coordinate> range;
    private final Orientation orientation;
    private final DiscreteDomain<Coordinate> discreteDomain;

    public Coordinates(@Nonnull final Coordinate start, @Nonnull final Coordinate end)
    {
        this.range = Range.closed(start, end);
        this.orientation = of(start, end);
        this.discreteDomain = Orientation.HORIZONTAL.equals(orientation) ? new HorizontalDiscreteDomain(range) : new VerticalDiscreteDomain(range);
    }

    private ContiguousSet<Coordinate> contiguousSet() { return ContiguousSet.create(this.range, this.discreteDomain); }

    public Coordinate start() { return this.range.lowerEndpoint(); }

    public Coordinate end() { return this.range.upperEndpoint(); }

    public Orientation orientation() { return this.orientation; }

    public boolean intersects(@Nonnull final Coordinates coordinates) { return this.range.isConnected(coordinates.range); }

    public Set<Coordinate> asSet() { return contiguousSet(); }

    public List<Coordinate> asList() { return contiguousSet().asList(); }

    public boolean contains(@Nonnull final Coordinate c) { return this.range.contains(c); }

    @Nonnull
    public Iterator<Coordinate> iterator() { return contiguousSet().iterator(); }

    public long size()
    {
        return this.discreteDomain.distance(this.range.lowerEndpoint(), this.range.upperEndpoint());
    }

    @Override
    public int compareTo(@Nonnull final Coordinates o)
    {
        if (this.range.lowerEndpoint().compareTo(o.range.lowerEndpoint()) == 0 && this.range.upperEndpoint().compareTo(o.range.upperEndpoint()) == 0)
        {
            return 0;
        }
        else if (this.range.lowerEndpoint().compareTo(o.range.lowerEndpoint()) < 0 || this.range.upperEndpoint().compareTo(o.range.upperEndpoint()) < 0)
        {
            return -1;
        }
        else if (this.range.lowerEndpoint().compareTo(o.range.lowerEndpoint()) > 0 || this.range.upperEndpoint().compareTo(o.range.upperEndpoint()) > 0)
        {
            return 1;
        }
        else
        {
            throw new IllegalArgumentException("This should never happen! Investigate why this is happening!");
        }
    }

    @Override public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final Coordinates that = (Coordinates) o;
        return this.toString().equals(that.toString());
    }

    @Override public int hashCode()
    {
        return Objects.hashCode(range, orientation, discreteDomain);
    }

    @Override public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("range", range)
                          .add("orientation", orientation)
                          .toString();
    }

    public static class Serializer extends JsonSerializer<Coordinates>
    {
        @Override public void serialize(final Coordinates value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException, JsonProcessingException
        {
            gen.writeStartObject();
            gen.writeObjectField("start", value.range.lowerEndpoint());
            gen.writeObjectField("end", value.range.upperEndpoint());
            gen.writeEndObject();
        }
    }

    public static class Builder implements Start<End<Build<Coordinates>, Coordinate>, Coordinate>
    {
        @Override public End<Build<Coordinates>, Coordinate> start(@Nonnull final Coordinate start)
        {
            return new End<Build<Coordinates>, Coordinate>()
            {
                @Override public Build<Coordinates> end(@Nonnull final Coordinate end)
                {
                    return new Build<Coordinates>()
                    {
                        @Override public Coordinates build()
                        {
                            return new Coordinates(start, end);
                        }
                    };
                }
            };
        }
    }

    public static class Deserializer extends JsonDeserializer<Coordinates>
    {
        @Override public Coordinates deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException, JsonProcessingException
        {
            final JsonNode n = p.readValueAsTree();
            return new Builder().start(p.getCodec().treeToValue(n.get("start"), Coordinate.class))
                                .end(p.getCodec().treeToValue(n.get("end"), Coordinate.class))
                                .build();
        }
    }
}
