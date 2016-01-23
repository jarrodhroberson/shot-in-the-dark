package com.vertigrated.sitd;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.vertigrated.fluent.Build;
import com.vertigrated.fluent.Player;
import com.vertigrated.fluent.Shots;
import com.vertigrated.sitd.board.Board;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class Game
{
    @JsonProperty
    public final UUID player;
    @JsonProperty
    public final Board board;
    @JsonProperty
    public final List<Shot> shots;

    public Game(@Nonnull final UUID player, @Nonnull final Board board, @Nonnull final List<Shot> shots)
    {
        this.player = player;
        this.board = board;
        this.shots = shots;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        final Game game = (Game) o;
        return Objects.equal(player, game.player);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(player);
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("player", player)
                          .add("board", board)
                          .add("shots", shots)
                          .toString();
    }

    interface GameBuilder extends Player<com.vertigrated.fluent.Board<Shots<Build<Game>>>, UUID> {}

    public static class Builder implements GameBuilder
    {
        private UUID player;
        @Override public com.vertigrated.fluent.Board<Shots<Build<Game>>> player(@Nonnull final UUID player)
        {
            this.player = player;
            return new com.vertigrated.fluent.Board<Shots<Build<Game>>>() {
                private Board board;
                @Override public Shots<Build<Game>> board(@Nonnull final Board board)
                {
                    this.board = board;
                    return new Shots<Build<Game>>() {
                        private List<Shot> shots;
                        @Override public Build<Game> shots(@Nonnull final List<Shot> shots)
                        {
                            this.shots = shots;
                            return new Build<Game>() {
                                @Override public Game build()
                                {
                                    return new Game(player,board,shots);
                                }
                            };
                        }
                    };
                }
            };
        }
    }
}
