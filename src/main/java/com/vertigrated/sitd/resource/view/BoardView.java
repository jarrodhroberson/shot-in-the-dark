package com.vertigrated.sitd.resource.view;

import static com.google.common.base.Charsets.UTF_8;

import javax.annotation.Nonnull;

import com.vertigrated.sitd.representation.Board;
import io.dropwizard.views.View;

public class BoardView extends View
{
    private final Board board;

    public BoardView(@Nonnull final Board board)
    {
        super("board.mustache", UTF_8);
        this.board = board;
    }

    public Board getBoard() { return this.board; }
}
