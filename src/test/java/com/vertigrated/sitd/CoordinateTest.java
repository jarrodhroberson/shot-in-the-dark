package com.vertigrated.sitd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSortedSet;
import org.junit.Test;

import java.util.Set;

import static com.vertigrated.sitd.Orientation.HORIZONTAL;
import static com.vertigrated.sitd.Orientation.VERTICAL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class CoordinateTest
{
    @Test
    public void testVerticalOrientationDetection()
    {
        final Coordinates c = new Coordinates(new Coordinate(0,0), new Coordinate(0,9));
        assertThat(VERTICAL, is(equalTo(c.orientation())));
    }

    @Test
    public void testHorizontalOrientationDetection()
    {
        final Coordinates c = new Coordinates(new Coordinate(0, 0), new Coordinate(9, 0));
        assertThat(HORIZONTAL, is(equalTo(c.orientation())));
    }

    @Test
    public void testHorizontalContigousSet()
    {
        final Coordinates c = new Coordinates(new Coordinate(0, 0), new Coordinate(9, 0));
        final Set<Coordinate> set = c.asSet();
        final ImmutableSortedSet.Builder<Coordinate> issb = ImmutableSortedSet.orderedBy(Coordinates.NATURAL_X);
        final int y = 0;
        for (int x=0; x < 10; x++)
        {
            issb.add(new Coordinate(x,y));
        }
        final Set<Coordinate> actual = issb.build();
        assertThat(actual, is(equalTo(set)));
    }

    @Test
    public void testVerticalContigousSet()
    {
        final Coordinates c = new Coordinates(new Coordinate(0, 0), new Coordinate(0, 9));
        final Set<Coordinate> set = c.asSet();
        final ImmutableSortedSet.Builder<Coordinate> issb = ImmutableSortedSet.orderedBy(Coordinates.NATURAL_Y);
        final int x = 0;
        for (int y = 0; y < 10; y++)
        {
            issb.add(new Coordinate(x, y));
        }
        final Set<Coordinate> actual = issb.build();
        assertThat(actual, is(equalTo(set)));
    }

    @Test
    public void testNaturalOrdering()
    {
        final Coordinate hl = new Coordinate(0, 0);
        final Coordinate hr = new Coordinate(9, 0);
        assertThat(hl, is(lessThan(hr)));
        assertThat(hr, is(greaterThan(hl)));
        assertThat(hr, is(equalTo(hr)));
        assertThat(hl, is(equalTo(hl)));

        final Coordinate vl = new Coordinate(0, 0);
        final Coordinate vr = new Coordinate(0, 9);
        assertThat(vl, is(lessThan(vr)));
        assertThat(vr, is(greaterThan(vl)));
        assertThat(vr, is(equalTo(vr)));
        assertThat(vl, is(equalTo(vl)));
    }

    @Test
    public void testSerializer() throws JsonProcessingException
    {
        final ObjectMapper om = new ObjectMapper();
        final Coordinate c = new Coordinate(0,0);
        final String json = om.writeValueAsString(c);
        assertThat("{\"x\":0,\"y\":0}", is(equalTo(json)));
    }

    @Test
    public void TestDeserializer() throws Exception
    {
        final ObjectMapper om = new ObjectMapper();
        final Coordinate c1 = new Coordinate(0, 0);
        final String json = om.writeValueAsString(c1);
        final Coordinate c2 = om.readValue(json,Coordinate.class);
        assertThat(c1, is(equalTo(c2)));
    }
}
