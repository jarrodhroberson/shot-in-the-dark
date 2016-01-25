package com.vertigrated.fluent;

import com.vertigrated.sitd.Shot;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public interface Shots<T>
{
    @Nonnull
    public T shots(@Nonnull final Shot... shots);

    @Nonnull
    public T shots(@Nonnull final List<Shot> shots);

    @Nonnull
    public T shots(@Nonnull final Set<Shot> shots);
}
