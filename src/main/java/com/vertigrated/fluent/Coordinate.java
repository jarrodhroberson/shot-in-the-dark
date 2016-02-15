package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Coordinate<T,N>
{
    @Nonnull
    public T coordinate(@Nonnull final N x, @Nonnull final N y);

    @Nonnull
    public T coordinate(@Nonnull final com.vertigrated.sitd.representation.Coordinate coordinate);
}
