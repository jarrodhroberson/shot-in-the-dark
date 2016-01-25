package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Coordinates<T>
{
    @Nonnull
    public T coordinates(@Nonnull com.vertigrated.sitd.Coordinates coordinates);
}
