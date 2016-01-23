package com.vertigrated.sitd.service;

import com.vertigrated.sitd.board.Board;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

public class DatabaseBoardService implements BoardService
{
    @Override public Board create(final int width, final int height)
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.DatabaseBoardService.create()");
    }

    @Override public Board retrieve(@Nonnull final UUID uuid)
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.DatabaseBoardService.retrieve()");
    }

    @Override public Set<Board> all()
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.DatabaseBoardService.all()");
    }
}
