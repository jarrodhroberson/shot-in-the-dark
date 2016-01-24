package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface End<T,C>
{
    public T end(@Nonnull final C end);
}
