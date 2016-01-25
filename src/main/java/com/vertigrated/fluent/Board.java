package com.vertigrated.fluent;

import javax.annotation.Nonnull;

public interface Board<T>
{
    @Nonnull
    public T board(@Nonnull final com.vertigrated.sitd.board.Board board);
}
