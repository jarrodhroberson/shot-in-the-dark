package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Start<T,C>
{
    public T start(@Nonnull final C start);
}
