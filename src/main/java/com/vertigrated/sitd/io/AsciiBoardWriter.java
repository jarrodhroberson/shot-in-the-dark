package com.vertigrated.sitd.io;

import com.google.common.base.Throwables;
import com.vertigrated.sitd.Target;
import com.vertigrated.sitd.board.Board;
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
                final boolean test = b.test(r, c);
                final Target t = b.at(r,c);
                L.debug("{}/{} : {}", r,c,t);
                this.write(test ? 'X' : this.empty);
            }
            this.write("\n");
        }
    }
}
