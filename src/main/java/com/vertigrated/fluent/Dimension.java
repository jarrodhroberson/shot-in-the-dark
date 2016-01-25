package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Dimension<T,N extends Number>
{
    @Nonnull
    public T dimension(@Nonnull final N width, @Nonnull final N height);

    @Nonnull
    public T dimension(@Nonnull final N side);
}
