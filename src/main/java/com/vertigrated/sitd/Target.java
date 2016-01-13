package com.vertigrated.sitd;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import java.util.List;

import static com.vertigrated.sitd.Orientation.HORIZONTAL;
import static com.vertigrated.sitd.Orientation.VERTICAL;

public class Target
{
    public final String name;
    public final List<Coordinate> coordinates;
    public final Orientation orientation;

    public Target(@Nonnull final String name, @Nonnull final List<Coordinate> coordinates)
    {
        this.name = name;
        this.coordinates = coordinates;
        this.orientation = coordinates.get(0).x.equals(coordinates.get(coordinates.size()-1).x) ? HORIZONTAL : VERTICAL;
    }

    public Integer size() { return this.coordinates.size(); }

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
        return this.name;
    }
}
