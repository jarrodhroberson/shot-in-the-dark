package com.vertigrated.sitd;

import com.vertigrated.sitd.board.Board;
import com.vertigrated.sitd.board.RandomTargetPlacementStrategy;
import com.vertigrated.sitd.io.AsciiBoardWriter;
import com.vertigrated.sitd.io.BoardWriter;
import org.junit.Test;

import java.io.OutputStreamWriter;

public class BoardTest
{
    private static Character EMPTY = '.';

    @Test
    public void createSquareBoard() throws Exception
    {
        final Board b = new Board.Builder().dimension(10).targets(new RandomTargetPlacementStrategy(2,5,3)).build();
        final OutputStreamWriter osw = new OutputStreamWriter(System.out);
        final BoardWriter bw = new AsciiBoardWriter(EMPTY,osw);
        bw.write(b);
        osw.close();
    }

    @Test
    public void createRandomTargets() throws Exception
    {
//        final Board b = Board.build(new Board(10), 2, 5, 5);
//        final OutputStreamWriter osw = new OutputStreamWriter(System.out);
//        final BoardWriter bw = new AsciiBoardWriter(EMPTY,osw);
//        bw.write(b);
//        osw.close();
    }
}
