package com.vertigrated.sitd;

import javax.validation.constraints.NotNull;

public interface BoardWriter
{
    public void write(@NotNull final Board b);
}
