package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Player<T,P>
{
    @Nonnull
    public T player(@Nonnull final P player);
}
