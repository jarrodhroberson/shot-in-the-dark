package com.vertigrated.sitd;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static com.vertigrated.sitd.Orientation.HORIZONTAL;
import static com.vertigrated.sitd.Orientation.VERTICAL;
import static java.lang.String.format;

public class Coordinate
{
    public static Ordering<Coordinate> NATURAL_X = new Ordering<Coordinate>() {
        @Override public int compare(@Nullable final Coordinate coordinate, @Nullable final Coordinate t1)
        {
            return coordinate.x.compareTo(t1.x);
        }
    };
    public static Ordering<Coordinate> NATURAL_Y = new Ordering<Coordinate>() {
        @Override public int compare(@Nullable final Coordinate coordinate, @Nullable final Coordinate t1)
        {
            return coordinate.y.compareTo(t1.y);
        }
    };

    public static Orientation orientation(@Nonnull final Coordinate start, @Nonnull final Coordinate end)
    {
        return start.x.equals(end.x) ? HORIZONTAL : VERTICAL;
    }

    public static List<Coordinate> rangeToList(@Nonnull final Coordinate start, @Nonnull final Coordinate end)
    {
        final ImmutableList.Builder<Coordinate> ilb = ImmutableList.builder();
        if (start.x.equals(end.x))
        {
            for (int y = start.y; y < end.y; y++)
            {
                ilb.add(new Coordinate(start.x, y));
            }
        }
        else
        {
            for (int x = start.x; x < end.x; x++)
            {
                ilb.add(new Coordinate(x, start.y));
            }
        }
        return ilb.build();
    }

    @JsonProperty
    public final Integer x;
    @JsonProperty
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

    @Override public int hashCode()
    {
        return Objects.hashCode(x, y);
    }

    @Override public String toString()
    {
        return format("%d:%d", this.x, this.y);
    }
}
