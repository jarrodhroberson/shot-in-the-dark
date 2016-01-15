package com.vertigrated.sitd;

import com.google.common.base.Throwables;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class AsciiBoardWriter implements BoardWriter
{
    private final Character empty;
    private final OutputStreamWriter osw;

    public AsciiBoardWriter(@Nonnull final OutputStreamWriter osw)
    {
        this('.', osw);
    }

    public AsciiBoardWriter(@Nonnull Character empty, @Nonnull final OutputStreamWriter osw)
    {
        this.empty = empty;
        this.osw = osw;
    }

    private void write(@Nonnull final Character c)
    {
        try { this.osw.write(c); }
        catch (final IOException e) { Throwables.propagate(e); }
    }

    private void write(@Nonnull final String s)
    {
        try { this.osw.write(s); }
        catch (final IOException e) { Throwables.propagate(e); }
    }

    @Override public void write(@Nonnull final Board b)
    {
        for (int r = 0; r < b.height; r++)
        {
            for (int c = 0; c < b.width; c++)
            {

                this.write(b.test(r,c) ? b.at(r,c) : this.empty);
            }
            this.write("\n");
        }
    }
}
