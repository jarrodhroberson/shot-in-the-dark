package com.vertigrated.sitd.representation;

import com.vertigrated.pattern.Strategy;

import javax.annotation.Nonnull;
import java.util.Set;

public class SetTargetPlacementStrategy implements Strategy<Board, Set<Target>>
{
    private final Set<Target> targets;

    public SetTargetPlacementStrategy(@Nonnull final Set<Target> targets)
    {
        this.targets = targets;
    }

    @Override
    public Set<Target> apply(@Nonnull final Board board)
    {
        return this.targets;
    }
}
