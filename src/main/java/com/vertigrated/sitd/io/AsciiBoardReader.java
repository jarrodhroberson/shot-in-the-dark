package com.vertigrated.sitd.io;

import com.vertigrated.sitd.board.Board;

import javax.annotation.Nonnull;
import java.io.InputStream;

public class AsciiBoardReader implements BoardReader
{
    @Override
    public Board read(@Nonnull final InputStream is)
    {
        throw new UnsupportedOperationException();
//        try
//        {
//            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            final List<String> lines = CharStreams.readLines(br);
//            final int width = lines.get(0).length();
//            final int height = lines.size();
//
//            for (final String l : lines)
//            {
//
//            }
//            return null;
//        }
//        catch (IOException e)
//        {
//            throw new RuntimeException(e);
//        }
    }
}
