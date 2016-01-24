package com.vertigrated.function;

import com.google.common.base.Function;

import javax.annotation.Nullable;

public class Numeric
{
    public static SumInteger sumInteger() { return new SumInteger(); }

    public static SumLong sumLong() { return new SumLong(); }

    public static class SumInteger implements Function<Iterable<Integer>, Integer>
    {
        private SumInteger() { /* intentionally blank */ }

        @Nullable @Override public Integer apply(@Nullable final Iterable<Integer> input)
        {
            int sum = 0;
            for (int i : input)
            {
                sum += i;
            }
            return sum;
        }
    }

    public static class SumLong implements Function<Iterable<Long>, Long>
    {
        private SumLong() { /* intentionally blank */ }

        @Nullable @Override public Long apply(@Nullable final Iterable<Long> input)
        {
            long sum = 0;
            for (long i : input)
            {
                sum += i;
            }
            return sum;
        }
    }
}
