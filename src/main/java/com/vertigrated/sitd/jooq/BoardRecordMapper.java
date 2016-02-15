package com.vertigrated.sitd.jooq;

import com.google.common.collect.ImmutableSortedSet;
import com.google.inject.Inject;
import com.vertigrated.sitd.jooq.tables.records.BoardRecord;
import com.vertigrated.sitd.representation.Board;
import com.vertigrated.sitd.representation.Target;
import org.jooq.DSLContext;
import org.jooq.RecordMapper;

import javax.annotation.Nonnull;
import java.util.List;

public class BoardRecordMapper implements RecordMapper<BoardRecord,Board>
{
    private DSLContext dsl;

    @Inject
    BoardRecordMapper(@Nonnull final DSLContext dsl)
    {
        this.dsl = dsl;
    }

    @Nonnull
    @Override
    public Board map(@Nonnull final BoardRecord record)
    {
        final List<Target> targets = this.dsl.selectFrom(Tables.TARGET).where(Tables.TARGET.BOARD.equal(record.getId())).fetchInto(Target.class);
        return new Board.Builder().dimension(record.getWidth(),record.getHeight()).targets(ImmutableSortedSet.copyOf(targets)).build();
    }
}
