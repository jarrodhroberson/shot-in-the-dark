package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Id<T,I>
{
    @Nonnull
    public T id(@Nonnull final I id);
}
