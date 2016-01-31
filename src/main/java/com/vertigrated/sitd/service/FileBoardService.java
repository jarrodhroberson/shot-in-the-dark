package com.vertigrated.sitd.service;

import com.vertigrated.pattern.Strategy;
import com.vertigrated.sitd.Target;
import com.vertigrated.sitd.board.Board;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

public class FileBoardService implements BoardService
{
    @Nonnull
    @Override
    public Board create(final int width, final int height, @Nonnull final Strategy<Board, Set<Target>> targetPlacementStrategy)
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.FileBoardService.create()");
    }

    @Nonnull
    @Override
    public Board retrieve(@Nonnull final UUID uuid)
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.FileBoardService.retrieve()");
    }

    @Nonnull
    @Override
    public Set<Board> all()
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.FileBoardService.all()");
    }
}
