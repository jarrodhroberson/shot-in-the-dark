package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface On<T,O>
{
    public T on(@Nonnull final O on);
}
