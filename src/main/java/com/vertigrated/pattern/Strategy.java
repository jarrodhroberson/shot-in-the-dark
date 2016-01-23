package com.vertigrated.pattern;

import javax.annotation.Nonnull;

public interface Strategy<T,R>
{
    public R apply(@Nonnull final T target);
}
