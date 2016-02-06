package com.vertigrated.sitd.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provider;
import com.vertigrated.sitd.Game;
import com.vertigrated.sitd.Shot;
import com.vertigrated.sitd.board.Board;
import com.vertigrated.sitd.jooq.Tables;
import com.vertigrated.sitd.jooq.tables.records.GameRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.Connection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DatabaseGameService extends DatabaseService implements GameService
{
    private static final Logger L = LoggerFactory.getLogger(DatabaseGameService.class);

    private static final com.vertigrated.sitd.jooq.tables.Game GAME = Tables.GAME;

    @Inject
    DatabaseGameService(@Nonnull final Provider<Connection> connectionProvider)
    {
        super(connectionProvider);
    }

    public Game create(@Nonnull final UUID player, @Nonnull final Board board)
    {
        final DSLContext dsl = DSL.using(super.connection());
        final Game g = new Game.Builder().id(UUID.randomUUID()).player(player).board(board).shots(ImmutableList.<Shot>of()).build();
        dsl.insertInto(GAME).set(GAME.BOARD, board.id.toString())
           .set(GAME.PLAYER, g.player.toString())
           .set(GAME.ID, g.id.toString())
           .execute();
        return g;
    }

    @Override
    public Set<Game> retrieve(@Nonnull final UUID player, @Nonnull final UUID board)
    {
        final DSLContext dsl = DSL.using(super.connection());
        final List<String> grs = dsl.selectFrom(GAME).where(GAME.PLAYER.equal(player.toString()))
                                    .and(GAME.BOARD.equal(board.toString())).fetch(GAME.ID);
        final ImmutableSet.Builder<Game> issb = ImmutableSet.builder();
        for (final String gr : grs)
        {
            issb.add(this.retrieve(UUID.fromString(gr)));
        }
        return issb.build();
    }

    @Override
    public Game retrieve(@Nonnull final UUID game)
    {
        final DSLContext dsl = DSL.using(super.connection());
        final Result<GameRecord> grs = dsl.selectFrom(GAME).where(GAME.ID.equal(game.toString())).fetch();

        return null;
    }
}
