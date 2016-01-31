package com.vertigrated.sitd.service;

import com.vertigrated.sitd.Game;

import javax.annotation.Nonnull;
import java.util.UUID;

public class FileGameService implements GameService
{
    @Override
    public Game create(@Nonnull final UUID user, @Nonnull final UUID board)
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.FileGameService.create()");
    }

    @Override
    public Game retrieve(@Nonnull final UUID game)
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.FileGameService.retrieve()");
    }
}
