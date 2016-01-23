package com.vertigrated.sitd.io;

import com.vertigrated.sitd.board.Board;

import javax.annotation.Nonnull;
import java.io.InputStream;

public interface BoardReader
{
    public Board read(@Nonnull final InputStream is);
}
