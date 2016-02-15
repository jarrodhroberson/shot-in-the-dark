package com.vertigrated.sitd.io;

import com.google.common.base.Throwables;
import com.vertigrated.sitd.representation.Board;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class AsciiBoardWriter implements BoardWriter
{
    private static final Logger L = LoggerFactory.getLogger(AsciiBoardWriter.class);

    private final Character empty;
    private final OutputStreamWriter osw;

    public AsciiBoardWriter(@Nonnull Character empty, @Nonnull final OutputStreamWriter osw)
    {
        this.empty = empty;
        this.osw = osw;
    }

    private void write(@Nonnull final Integer i)
    {
        try
        {
            this.osw.write(String.valueOf(i));
            this.osw.flush();
        }
        catch (final IOException e) { Throwables.propagate(e); }
    }

    private void write(@Nonnull final Character c)
    {
        try
        {
            this.osw.write(c);
            this.osw.flush();
        }
        catch (final IOException e) { Throwables.propagate(e); }
    }

    private void write(@Nonnull final String s)
    {
        try
        {
            this.osw.write(s);
            this.osw.flush();
        }
        catch (final IOException e) { Throwables.propagate(e); }
    }

    @Override public void write(@Nonnull final Board b)
    {
        this.write("  0123456789\n");
        for (int row = 0; row < b.height; row++)
        {
            this.write(row);
            this.write('|');
            for (int column = 0; column < b.width; column++)
            {
                if (b.test(column, row))
                {
                    this.write('@');
                }
                else
                {
                    this.write(this.empty);
                }
            }
            this.write('\n');
        }
    }
}
