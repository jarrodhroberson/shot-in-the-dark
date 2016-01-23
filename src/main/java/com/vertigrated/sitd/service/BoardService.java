package com.vertigrated.sitd.service;

import com.vertigrated.sitd.board.Board;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

public interface BoardService
{
    public Board create(final int width, final int height);
    public Board retrieve(@Nonnull final UUID uuid);
    public Set<Board> all();
}
