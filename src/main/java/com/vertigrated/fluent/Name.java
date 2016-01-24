package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Name<T>
{
    public T name(@Nonnull String name);
}
