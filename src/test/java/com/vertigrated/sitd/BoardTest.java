package com.vertigrated.sitd;

import org.junit.Test;

import java.io.OutputStreamWriter;

public class BoardTest
{
    @Test
    public void createSquareBoard() throws Exception
    {
        final Board b = new Board(10);
        final OutputStreamWriter osw = new OutputStreamWriter(System.out);
        final BoardWriter bw = new AsciiBoardWriter(osw);
        bw.write(b);
        osw.close();
    }

    @Test
    public void createRandomTargets() throws Exception
    {
        final Board b = Board.build(new Board(10), 2, 5, 5);
        final OutputStreamWriter osw = new OutputStreamWriter(System.out);
        final BoardWriter bw = new AsciiBoardWriter(osw);
        bw.write(b);
        osw.close();
    }
}
