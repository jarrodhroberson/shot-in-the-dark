package com.vertigrated.sitd;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableSet;
import com.vertigrated.sitd.board.Board;
import com.vertigrated.sitd.board.RandomTargetPlacementStrategy;
import com.vertigrated.sitd.io.AsciiBoardWriter;
import com.vertigrated.sitd.io.BoardWriter;
import org.junit.Test;

import java.io.OutputStreamWriter;
import java.util.Set;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

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
        final Stopwatch sw = Stopwatch.createStarted();
        final RandomTargetPlacementStrategy strategy = new RandomTargetPlacementStrategy(2, 8, 5);
        final Board b = new Board.Builder().dimension(10).targets(strategy).build();
        assertThat(MILLISECONDS.convert(10, SECONDS), is(greaterThan(sw.stop().elapsed(MILLISECONDS))));
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
