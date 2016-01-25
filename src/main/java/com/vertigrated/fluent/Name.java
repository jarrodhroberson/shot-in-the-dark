package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Name<T>
{
    @Nonnull
    public T name(@Nonnull String name);
}
