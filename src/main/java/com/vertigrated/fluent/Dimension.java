package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Dimension<T>
{
    public T dimension(@Nonnull final Integer width, @Nonnull final Integer height);
    public T dimension(@Nonnull final Integer side);
}
