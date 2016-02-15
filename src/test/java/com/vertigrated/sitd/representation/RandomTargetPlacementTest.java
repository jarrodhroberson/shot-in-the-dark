package com.vertigrated.sitd.representation;

import com.google.common.base.Stopwatch;
import com.vertigrated.sitd.representation.Board;
import com.vertigrated.sitd.representation.RandomTargetPlacementStrategy;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.vertigrated.function.Numeric.sumLong;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;

public class RandomTargetPlacementTest
{
    @Test
    public void test10X10_2_5_8()
    {
        this.testPerformance(10,2,5,8);
    }

    @Test
    public void test10Times()
    {
        this.times(10);
    }

    @Test
    public void test100Times()
    {
        this.times(100);
    }

    @Test
    public void test1000Times()
    {
        this.times(1000);
    }

    private void times(final int times)
    {
        final List<Long> elapsedTimes = new ArrayList<>();
        final Stopwatch sw = Stopwatch.createUnstarted();
        for (int i = 0; i < times; i++)
        {
            sw.start();
            test10X10_2_5_8();
            final long elapsed = sw.elapsed(MILLISECONDS);
            sw.reset();
            elapsedTimes.add(elapsed);
            assertThat(MILLISECONDS.toMillis(300), is(greaterThanOrEqualTo(elapsed)));
        }
        assertThat(MILLISECONDS.toMillis(300*times), is(greaterThanOrEqualTo(sumLong().apply(elapsedTimes))));
    }

    private void testPerformance(final int dimension, final int minTargetSize, final int maxTargetSize, final int targetCount)
    {
        final RandomTargetPlacementStrategy strategy = new RandomTargetPlacementStrategy(minTargetSize, maxTargetSize, targetCount);
        new Board.Builder().dimension(dimension).targets(strategy).build();
    }
}
