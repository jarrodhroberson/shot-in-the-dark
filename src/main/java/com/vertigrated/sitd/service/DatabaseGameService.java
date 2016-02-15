package com.vertigrated.sitd.service;

import java.sql.Timestamp;
import java.util.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.sql.DataSource;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Provider;
import com.vertigrated.jooq.StringUuidConverter;
import com.vertigrated.sitd.jooq.GameRecordMapper;
import com.vertigrated.sitd.jooq.ShotRecordMapper;
import com.vertigrated.sitd.jooq.Tables;
import com.vertigrated.sitd.representation.*;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseGameService extends DatabaseService implements GameService
{
    private static final Logger L = LoggerFactory.getLogger(DatabaseGameService.class);

    private static final com.vertigrated.sitd.jooq.tables.Game GAME = Tables.GAME;
    private static final com.vertigrated.sitd.jooq.tables.Shot SHOT = Tables.SHOT;

    private final BoardService boardService;

    @Inject
    DatabaseGameService(@Nonnull final Provider<DataSource> dataSourceProvider, @Nonnull final BoardService boardService)
    {
        super(dataSourceProvider);
        this.boardService = boardService;
    }

    public Game create(@Nonnull final UUID player, @Nonnull final UUID board)
    {
        final DSLContext dsl = DSL.using(super.connection());
        final Game g = new Game.Builder().id(UUID.randomUUID()).player(player).board(board).build();
        dsl.insertInto(GAME).set(GAME.BOARD, board.toString())
           .set(GAME.PLAYER, g.player.toString())
           .set(GAME.ID, g.id.toString())
           .execute();
        return this.byId(g.id);
    }

    @Override
    public void recordShot(@Nonnull final UUID game, @Nonnull final Shot shot)
    {
        final DSLContext dsl = DSL.using(super.connection());
        dsl.insertInto(SHOT).set(SHOT.GAME, game.toString())
           .set(SHOT.X, shot.coordinate.x)
           .set(SHOT.Y, shot.coordinate.y)
           .set(SHOT.ORDINAL, new Timestamp(shot.placed.getTime()))
           .execute();
    }

    @Override
    public List<Shot> shotsByGame(@Nonnull final UUID game)
    {
        final DSLContext dsl = DSL.using(super.connection());
        return dsl.selectFrom(SHOT).where(SHOT.GAME.equal(game.toString())).orderBy(SHOT.ORDINAL).fetch(new ShotRecordMapper());
    }

    @Override
    public Set<UUID> byPlayerBoard(@Nonnull final UUID player, @Nonnull final UUID board)
    {
        final DSLContext dsl = DSL.using(super.connection());
        return ImmutableSortedSet.copyOf(dsl.selectFrom(GAME).where(GAME.PLAYER.equal(player.toString()))
                                            .and(GAME.BOARD.equal(board.toString())).fetchSet(GAME.ID, new StringUuidConverter()));
    }

    @Override
    public Game byId(@Nonnull final UUID game)
    {
        final DSLContext dsl = DSL.using(super.connection());
        return dsl.selectFrom(GAME).where(GAME.ID.equal(game.toString())).orderBy(GAME.ID).fetchOne(new GameRecordMapper());
    }

    @Override
    public Set<Game> byIds(@Nonnull final Collection<UUID> games)
    {
        final DSLContext dsl = DSL.using(super.connection());
        return ImmutableSortedSet.copyOf(dsl.selectFrom(GAME).where(GAME.ID.in(games)).orderBy(GAME.ID).fetch(new GameRecordMapper()));
    }

    @Override
    public Set<UUID> byPlayer(@Nonnull final UUID player)
    {
        final DSLContext dsl = DSL.using(super.connection());
        return dsl.selectFrom(GAME).where(GAME.PLAYER.equal(player.toString())).orderBy(GAME.ID).fetchSet(GAME.ID, new StringUuidConverter());
    }

    @Override
    public Set<UUID> all()
    {
        final DSLContext dsl = DSL.using(super.connection());
        return dsl.selectFrom(GAME).orderBy(GAME.ID).fetchSet(GAME.ID, new StringUuidConverter());
    }

    @Override
    public boolean isComplete(@Nonnull final UUID game)
    {
        final Game g = this.byId(game);
        final Board b = this.boardService.retrieve(g.board);
        final Map<Coordinate,Boolean> targets = this.targetsToMapOfHits(b.targets);
        final List<Coordinate> shots = Lists.transform(this.shotsByGame(game), new Function<Shot, Coordinate>() {
            @Nullable
            @Override
            public Coordinate apply(@Nonnull final Shot input)
            {
                return input.coordinate;
            }
        });
        return Maps.filterEntries(targets, new Predicate<Map.Entry<Coordinate, Boolean>>() {
            @Override
            public boolean apply(@Nonnull final Map.Entry<Coordinate, Boolean> input)
            {
                return shots.contains(input.getKey());
            }
        }).isEmpty();
    }

    private Map<Coordinate,Boolean> targetsToMapOfHits(@Nonnull final Set<Target> targets)
    {
        final ImmutableMap.Builder<Coordinate,Boolean> imb = ImmutableMap.builder();
        for (final Target t : targets)
        {
            for (final Coordinate c : t.coordinates)
            {
                imb.put(c,false);
            }
        }
        return imb.build();
    }
}
