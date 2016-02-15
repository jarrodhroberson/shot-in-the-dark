package com.vertigrated.sitd.service;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vertigrated.pattern.Strategy;
import com.vertigrated.sitd.jooq.Tables;
import com.vertigrated.sitd.jooq.tables.records.BoardRecord;
import com.vertigrated.sitd.jooq.tables.records.TargetRecord;
import com.vertigrated.sitd.representation.Board;
import com.vertigrated.sitd.representation.Coordinate;
import com.vertigrated.sitd.representation.Coordinates;
import com.vertigrated.sitd.representation.Target;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.util.Set;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class DatabaseBoardService extends DatabaseService implements BoardService
{
    private static final com.vertigrated.sitd.jooq.tables.Board BOARD = Tables.BOARD;
    private static final com.vertigrated.sitd.jooq.tables.Target TARGET = Tables.TARGET;

    @Inject
    DatabaseBoardService(@Nonnull final Provider<DataSource> connectionProvider)
    {
        super(connectionProvider);
    }

    @Nonnull
    @Override
    public Board create(final int width, final int height, @Nonnull final Strategy<Board, Set<Target>> targetPlacementStrategy)
    {
        final Board b = new Board.Builder().dimension(width, height).targets(targetPlacementStrategy).build();
        final DSLContext dsl = DSL.using(super.connection());
        dsl.insertInto(BOARD)
           .set(BOARD.ID, b.id.toString())
           .set(BOARD.WIDTH, b.width)
           .set(BOARD.HEIGHT, b.height).execute();
        for (final Target t : b.targets)
        {
            dsl.insertInto(TARGET).set(TARGET.BOARD, b.id.toString())
               .set(TARGET.START_X, t.coordinates.start().x)
               .set(TARGET.START_Y, t.coordinates.start().y)
               .set(TARGET.END_X, t.coordinates.end().x)
               .set(TARGET.END_Y, t.coordinates.end().y)
               .execute();
        }
        return b;
    }

    @Nonnull
    @Override
    public Board retrieve(@Nonnull final UUID uuid)
    {
        final DSLContext dsl = DSL.using(super.connection());

        final BoardRecord br = dsl.selectFrom(BOARD).where(BOARD.ID.equal(uuid.toString())).fetchOne();
        final Result<TargetRecord> trs = dsl.selectFrom(TARGET).where(TARGET.BOARD.equal(uuid.toString())).fetch();
        return new Board.Builder().dimension(br.getWidth(), br.getHeight()).targets(ImmutableSortedSet.copyOf(Lists.transform(trs, new Function<TargetRecord, Target>()
        {
            @Nullable
            @Override
            public Target apply(@Nullable final TargetRecord tr)
            {
                final Coordinate start = new Coordinate(checkNotNull(tr).getStartX(), tr.getStartY());
                final Coordinate end = new Coordinate(tr.getEndX(), tr.getEndY());
                return new Target(new Coordinates.Builder().start(start).end(end).build());
            }
        }))).build();
    }

    @Nonnull
    @Override
    public Set<Board> all()
    {
        final DSLContext dsl = DSL.using(super.connection());
        final ImmutableSet.Builder<Board> issb = ImmutableSet.builder();
        final Result<BoardRecord> brs = dsl.selectFrom(BOARD).fetch();
        for (final BoardRecord br : brs)
        {
            final Result<TargetRecord> trs = dsl.selectFrom(TARGET).where(TARGET.BOARD.equal(br.getId())).fetch();
            issb.add(new Board.Builder().dimension(br.getWidth(), br.getHeight()).targets(ImmutableSortedSet.copyOf(Lists.transform(trs, new Function<TargetRecord, Target>()
            {
                @Nullable
                @Override
                public Target apply(@Nullable final TargetRecord tr)
                {
                    final Coordinate start = new Coordinate(checkNotNull(tr).getStartX(), tr.getStartY());
                    final Coordinate end = new Coordinate(tr.getEndX(), tr.getEndY());
                    return new Target(new Coordinates.Builder().start(start).end(end).build());
                }
            }))).build());
        }
        return issb.build();
    }
}
