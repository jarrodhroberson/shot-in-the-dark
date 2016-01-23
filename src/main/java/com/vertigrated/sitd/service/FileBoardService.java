package com.vertigrated.sitd.service;

import com.vertigrated.sitd.board.Board;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

public class FileBoardService implements BoardService
{
    @Override public Board create(final int width, final int height)
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.FileBoardService.create()");
    }

    @Override public Board retrieve(@Nonnull final UUID uuid)
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.FileBoardService.retrieve()");
    }

    @Override public Set<Board> all()
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.FileBoardService.all()");
    }
}
