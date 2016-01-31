package com.vertigrated.sitd.service;

import com.vertigrated.sitd.Game;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface GameService
{
    public Game create(@Nonnull final UUID user, @Nonnull final UUID board);
    public Game retrieve(@Nonnull final UUID game);
}
