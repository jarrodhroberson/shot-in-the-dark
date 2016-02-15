package com.vertigrated.fluent;

import java.util.UUID;
import javax.annotation.Nonnull;

public interface Board<T>
{
    @Nonnull
    public T board(@Nonnull final UUID board);
}
