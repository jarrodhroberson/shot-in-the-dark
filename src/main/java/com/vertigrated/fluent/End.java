package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface End<T,E>
{
    @Nonnull
    public T end(@Nonnull final E end);
}
