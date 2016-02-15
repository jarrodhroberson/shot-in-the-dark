package com.vertigrated.sitd.representation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.io.Resources;
import com.vertigrated.sitd.representation.Board;
import com.vertigrated.sitd.representation.Coordinate;
import com.vertigrated.sitd.representation.Coordinates;
import com.vertigrated.sitd.representation.Target;
import org.junit.Test;

import java.io.File;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class BoardTest
{
    private static Character EMPTY = '.';

    @Test
    public void testBoardTest()
    {
        final Set<Target> targets = ImmutableSet.of(new Target(new Coordinates(new Coordinate(0, 0), new Coordinate(9, 0))));
        final Board b = new Board.Builder().dimension(10).targets(targets).build();
        for (int x = 0; x < 10; x++)
        {
            assertThat("Target does not contain coordinate", b.test(x, 0));
        }
    }

    @Test
    public void createSquareBoard() throws Exception
    {
        final ObjectMapper om = new ObjectMapper();
        final Board b = om.readValue(new File(Resources.getResource("0881be1b-e56c-38e4-a48b-cf26f9f8c705.json").toURI()),Board.class);
    }

    @Test
    public void testSerializer() throws Exception
    {
        final Set<Target> targets = ImmutableSortedSet.of(new Target(new Coordinates(new Coordinate(0, 0), new Coordinate(9, 0))));
        final Board b = new Board.Builder().dimension(10).targets(targets).build();
        final ObjectMapper om = new ObjectMapper();
        final String json = om.writeValueAsString(b);
        assertThat("{\"id\":\"1cfed0e9-2476-3088-845d-58ad79d7961e\",\"width\":10,\"height\":10,\"targets\":[{\"name\":\"t1\",\"coordinates\":{\"start\":{\"x\":0,\"y\":0},\"end\":{\"x\":9,\"y\":0}}}]}", is(equalTo(json)));
    }

    @Test
    public void testDeserializer() throws Exception
    {
        final Set<Target> targets = ImmutableSortedSet.of(new Target(new Coordinates(new Coordinate(0, 0), new Coordinate(9, 0))));
        final Board b1 = new Board.Builder().dimension(10).targets(targets).build();
        final ObjectMapper om = new ObjectMapper();
        final String json = om.writeValueAsString(b1);
        final Board b2 = om.readValue(json,Board.class);
        assertThat(b1,is(equalTo(b2)));
    }
}
