package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Height<T, N extends Number>
{
    @Nonnull
    public T height(@Nonnull final N height);
}
