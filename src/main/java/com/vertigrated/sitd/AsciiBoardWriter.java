package com.vertigrated.sitd;

import com.google.common.base.Throwables;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class AsciiBoardWriter implements BoardWriter
{
    private final OutputStreamWriter osw;

    public AsciiBoardWriter(@Nonnull final OutputStreamWriter osw)
    {
        this.osw = osw;
    }

    private void write(@Nonnull final String s)
    {
        try { this.osw.write(s); }
        catch (final IOException e) { Throwables.propagate(e); }
    }

    @Override public void write(@Nonnull final Board b)
    {
        for (int r = 1; r < b.height; r++)
        {
            for (int c = 1; c < b.width; c++)
            {
                this.write(b.test(r, c) ? "*" : "O");
            }
            this.write("\n");
        }
    }
}
