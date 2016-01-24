package com.vertigrated.sitd;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Ordering;
import com.google.common.collect.Range;
import com.vertigrated.sitd.Coordinate.HorizontalDiscreteDomain;
import com.vertigrated.sitd.Coordinate.VerticalDiscreteDomain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

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
        return Objects.equal(range, that.range) &&
               orientation == that.orientation;
    }

    @Override public int hashCode()
    {
        return Objects.hashCode(range, orientation);
    }

    @Override public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("range", range)
                          .add("orientation", orientation)
                          .toString();
    }
}
