package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Width<T,N extends Number>
{
    @Nonnull
    public T width(@Nonnull final N width);
}
