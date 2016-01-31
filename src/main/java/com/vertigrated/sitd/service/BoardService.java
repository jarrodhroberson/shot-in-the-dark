package com.vertigrated.sitd.service;

import com.vertigrated.pattern.Strategy;
import com.vertigrated.sitd.Target;
import com.vertigrated.sitd.board.Board;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

public interface BoardService
{
    @Nonnull
    public Board create(final int width, final int height, @Nonnull final Strategy<Board, Set<Target>> targetPlacementStrategy);
    @Nonnull
    public Board retrieve(@Nonnull final UUID uuid);
    @Nonnull
    public Set<Board> all();
}
