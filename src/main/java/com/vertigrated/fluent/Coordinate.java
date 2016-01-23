package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Coordinate<T,N>
{
    public T coordinate(@Nonnull final N x, @Nonnull final N y);
    public T coordinate(@Nonnull final com.vertigrated.sitd.Coordinate coordinate);
}
