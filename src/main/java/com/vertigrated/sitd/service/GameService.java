package com.vertigrated.sitd.service;

import com.vertigrated.sitd.Game;
import com.vertigrated.sitd.board.Board;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

public interface GameService
{
    public Game create(@Nonnull final UUID user, @Nonnull final Board board);
    public Set<Game> retrieve(@Nonnull final UUID user, @Nonnull final UUID board);
    public Game retrieve(@Nonnull final UUID game);
}
