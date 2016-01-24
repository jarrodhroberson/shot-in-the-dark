package com.vertigrated.sitd;

import com.google.common.collect.ImmutableSet;
import com.vertigrated.sitd.board.Board;
import com.vertigrated.sitd.board.RandomTargetPlacementStrategy;
import com.vertigrated.sitd.io.AsciiBoardWriter;
import com.vertigrated.sitd.io.BoardWriter;
import org.junit.Test;

import java.io.OutputStreamWriter;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

public class BoardTest
{
    private static Character EMPTY = '.';

    @Test
    public void testBoardTest()
    {
        final Set<Target> targets = ImmutableSet.of(new Target("t1", new Coordinates(new Coordinate(0, 0), new Coordinate(9, 0))));
        final Board b = new Board.Builder().dimension(10).targets(targets).build();
        for (int x = 0; x < 10; x++)
        {
            assertThat("Target does not contain coordinate", b.test(x, 0));
        }
    }

    @Test
    public void createSquareBoard() throws Exception
    {
        final Board b = new Board.Builder().dimension(1000).targets(new RandomTargetPlacementStrategy(3, 3, 250)).build();
        final OutputStreamWriter osw = new OutputStreamWriter(System.out);
        final BoardWriter bw = new AsciiBoardWriter(EMPTY, osw);
        bw.write(b);
        osw.close();
    }

    @Test
    public void createRandomTargets() throws Exception
    {
//        final Board b = Board.build(new Board(10), 2, 5, 5);
//        final OutputStreamWriter osw = new OutputStreamWriter(System.out);
//        final BoardWriter bw = new AsciiBoardWriter(EMPTY,osw);
//        bw.write(b);
//        osw.close();
    }
}
