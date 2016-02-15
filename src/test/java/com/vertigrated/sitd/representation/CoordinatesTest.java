package com.vertigrated.sitd.representation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vertigrated.sitd.representation.Coordinate;
import com.vertigrated.sitd.representation.Coordinates;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CoordinatesTest
{
    @Test
    public void testSerializer() throws Exception
    {
        final ObjectMapper om = new ObjectMapper();
        final Coordinates c = new Coordinates(new Coordinate(0,0), new Coordinate(9,0));
        final String json = om.writeValueAsString(c);
        assertThat("{\"start\":{\"x\":0,\"y\":0},\"end\":{\"x\":9,\"y\":0}}", is(equalTo(json)));
    }

    @Test
    public void testDeserializer() throws Exception
    {
        final ObjectMapper om = new ObjectMapper();
        final Coordinates c1 = new Coordinates(new Coordinate(0, 0), new Coordinate(9, 0));
        final String json = om.writeValueAsString(c1);
        final Coordinates c2 = om.readValue(json, Coordinates.class);
        assertThat(c1, is(equalTo(c2)));
    }
}
