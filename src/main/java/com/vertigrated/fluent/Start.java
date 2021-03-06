package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Start<T,S>
{
    @Nonnull
    public T start(@Nonnull final S start);
}
