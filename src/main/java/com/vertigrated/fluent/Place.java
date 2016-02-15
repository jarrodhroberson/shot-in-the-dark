package com.vertigrated.fluent;

import com.vertigrated.sitd.representation.Target;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public interface Place<T>
{
    @Nonnull
    public T place(@Nonnull Target... targets);

    @Nonnull
    public T place(@Nonnull List<Target> targets);

    @Nonnull
    public T place(@Nonnull Set<Target> targets);
}
