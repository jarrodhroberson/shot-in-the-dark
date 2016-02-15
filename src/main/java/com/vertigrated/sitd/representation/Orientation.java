package com.vertigrated.sitd.representation;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public enum Orientation
{
    HORIZONTAL(new Ordering<Orientation>()
    {
        @Override
        public int compare(@Nullable final Orientation left, @Nullable final Orientation right)
        {
            return Ints.compare(checkNotNull(left).ordinal(), checkNotNull(right).ordinal());
        }
    }),
    VERTICAL(new Ordering<Orientation>()
    {
        @Override
        public int compare(@Nullable final Orientation left, @Nullable final Orientation right)
        {
            return Ints.compare(checkNotNull(right).ordinal(), checkNotNull(left).ordinal());
        }
    });

    public final Ordering<Orientation> ordering;

    private Orientation(final Ordering<Orientation> ordering)
    {
        this.ordering = ordering;
    }
}
