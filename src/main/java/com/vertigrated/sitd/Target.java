package com.vertigrated.sitd;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.Ordering;
import com.google.common.collect.Range;
import com.google.common.primitives.Ints;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.vertigrated.sitd.Orientation.HORIZONTAL;

public class Target implements Comparable<Target>
{
    public static final Ordering<Target> NAME = new Ordering<Target>()
    {
        @Override public int compare(@Nullable final Target left, @Nullable final Target right)
        {
            return checkNotNull(left).name.compareTo(checkNotNull(right).name);
        }
    };

    public static final Ordering<Target> SIZE = new Ordering<Target>()
    {
        @Override public int compare(@Nullable final Target left, @Nullable final Target right)
        {
            return Ints.compare(checkNotNull(left).size(), checkNotNull(right).size());
        }
    };

    public static final Ordering<Target> COORDINATES = new Ordering<Target>()
    {
        @Override public int compare(@Nullable final Target left, @Nullable final Target right)
        {
            return Coordinate.NATURAL.compare(checkNotNull(left).coordinates.lowerEndpoint(), checkNotNull(right).coordinates.lowerEndpoint());
        }
    };

    public final String name;
    public final Range<Coordinate> coordinates;
    public final Orientation orientation;

    public Target(@Nonnull final String name, @Nonnull final Range<Coordinate> coordinates)
    {
        this.name = name;
        this.coordinates = coordinates;
        this.orientation = Coordinate.from(coordinates);
    }

    public boolean intersects(@Nonnull final Target target)
    {
        return this.coordinates.isConnected(target.coordinates);
    }

    public boolean contains(@Nonnull final Coordinate coordinate)
    {
        return this.coordinates.contains(coordinate);
    }

    public Integer size()
    {
        if (this.orientation.equals(HORIZONTAL)) { return this.coordinates.upperEndpoint().x - this.coordinates.lowerEndpoint().x; }
        else { return this.coordinates.upperEndpoint().y - this.coordinates.lowerEndpoint().y; }
    }

    @Override
    public int compareTo(@Nonnull final Target o)
    {
        return SIZE.compound(COORDINATES).compound(NAME).compare(this, o);
    }

    @Override public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final Target target = (Target) o;
        return Objects.equal(name, target.name) &&
               Objects.equal(coordinates, target.coordinates) &&
               orientation == target.orientation;
    }

    @Override public int hashCode()
    {
        return Objects.hashCode(name, coordinates, orientation);
    }

    @Override public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("name", name)
                          .add("coordinates", coordinates)
                          .add("orientation", orientation)
                          .toString();
    }
}
