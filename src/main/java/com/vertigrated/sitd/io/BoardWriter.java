package com.vertigrated.sitd.io;

import com.vertigrated.sitd.representation.Board;

import javax.validation.constraints.NotNull;

public interface BoardWriter
{
    public void write(@NotNull final Board b);
}
