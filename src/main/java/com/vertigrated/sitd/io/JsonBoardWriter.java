package com.vertigrated.sitd.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vertigrated.sitd.representation.Board;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.OutputStream;

public class JsonBoardWriter implements BoardWriter
{
    private final ObjectMapper om;
    private final OutputStream os;

    @Inject
    JsonBoardWriter(@Nonnull final ObjectMapper objectMapper, @Nonnull final OutputStream outputStream)
    {
        this.om = objectMapper;
        this.os = outputStream;
    }

    @Override
    public void write(@NotNull final Board b)
    {

    }
}
