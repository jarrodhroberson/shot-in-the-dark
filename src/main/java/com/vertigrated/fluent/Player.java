package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Player<T,I>
{
    public T player(@Nonnull final I player);
}
