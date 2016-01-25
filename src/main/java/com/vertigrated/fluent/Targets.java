package com.vertigrated.fluent;

import com.vertigrated.pattern.Strategy;
import com.vertigrated.sitd.Target;

import javax.annotation.Nonnull;
import java.util.Set;

public interface Targets<T>
{
    @Nonnull
    public T targets(@Nonnull final Set<Target> targets);

    @Nonnull
    public T targets(@Nonnull final Strategy<com.vertigrated.sitd.board.Board,Set<Target>> strategy);
}
