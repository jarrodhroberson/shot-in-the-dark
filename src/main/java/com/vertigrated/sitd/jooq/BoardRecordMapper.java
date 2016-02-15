package com.vertigrated.sitd.jooq;

import java.util.HashSet;
import javax.annotation.Nonnull;

import com.vertigrated.sitd.jooq.tables.records.BoardRecord;
import com.vertigrated.sitd.representation.Board;
import org.jooq.RecordMapper;

public class BoardRecordMapper implements RecordMapper<BoardRecord, Board>
{
    @Nonnull
    @Override
    public Board map(@Nonnull final BoardRecord record)
    {
        return new Board.Builder().dimension(record.getWidth(), record.getHeight()).targets(new HashSet<>()).build();
    }
}
