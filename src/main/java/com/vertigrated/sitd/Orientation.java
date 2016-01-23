package com.vertigrated.sitd;

import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public enum Orientation
{
    HORIZONTAL, VERTICAL;

    public static Ordering<Orientation> HV = new Ordering<Orientation>() {
        @Override public int compare(@Nullable final Orientation left, @Nullable final Orientation right)
        {
            return Ints.compare(checkNotNull(left).ordinal(), checkNotNull(right).ordinal());
        }
    };
    public static Ordering<Orientation> VH = new Ordering<Orientation>()
    {
        @Override public int compare(@Nullable final Orientation left, @Nullable final Orientation right)
        {
            return Ints.compare(checkNotNull(right).ordinal(), checkNotNull(left).ordinal());
        }
    };
}
