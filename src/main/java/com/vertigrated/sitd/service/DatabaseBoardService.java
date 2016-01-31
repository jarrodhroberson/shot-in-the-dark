package com.vertigrated.sitd.service;

import com.vertigrated.pattern.Strategy;
import com.vertigrated.sitd.Target;
import com.vertigrated.sitd.board.Board;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

public class DatabaseBoardService implements BoardService
{
    @Nonnull
    @Override
    public Board create(final int width, final int height, @Nonnull final Strategy<Board, Set<Target>> targetPlacementStrategy)
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.DatabaseBoardService.create()");
    }

    @Nonnull
    @Override
    public Board retrieve(@Nonnull final UUID uuid)
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.DatabaseBoardService.retrieve()");
    }

    @Nonnull
    @Override
    public Set<Board> all()
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.DatabaseBoardService.all()");
    }
}
