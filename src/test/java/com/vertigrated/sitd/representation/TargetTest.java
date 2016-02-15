package com.vertigrated.sitd.representation;

import com.google.common.collect.ImmutableSortedSet;
import com.vertigrated.sitd.representation.Coordinate;
import com.vertigrated.sitd.representation.Coordinates;
import com.vertigrated.sitd.representation.Target;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

public class TargetTest
{
    @Test
    public void testTargetIntersects()
    {
        final Target t1 = new Target(new Coordinates(new Coordinate(0,0), new Coordinate(5,0)));
        final Target t2 = new Target(new Coordinates(new Coordinate(5,0), new Coordinate(9,0)));
        assertThat("t1 does not intersect t2", t1.intersects(t2));
    }

    @Test
    public void testTargetContainsCoordinates()
    {
        final Target t1 = new Target(new Coordinates(new Coordinate(0, 0), new Coordinate(5, 0)));
        for (final Coordinate c : t1.coordinates)
        {
            assertThat("t1 does not contain c", t1.contains(c));
        }
    }

    @Test
    public void testTargetSetToCoordinateMap()
    {
        final Set<Target> targets = ImmutableSortedSet.<Target>naturalOrder()
                .add(new Target(new Coordinates(new Coordinate(0,0), new Coordinate(5,0))))
                .add(new Target(new Coordinates(new Coordinate(0,1), new Coordinate(5,1))))
                .add(new Target(new Coordinates(new Coordinate(0,2), new Coordinate(5,2))))
                .build();

        final Map<Coordinate,Target> coordinateTargetMap;
    }
}
