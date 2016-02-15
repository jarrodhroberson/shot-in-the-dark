package com.vertigrated.sitd.jooq;

import com.vertigrated.sitd.jooq.tables.records.ShotRecord;
import com.vertigrated.sitd.representation.Shot;
import org.jooq.RecordMapper;

import javax.annotation.Nonnull;

public class ShotRecordMapper implements RecordMapper<ShotRecord,Shot>
{
    @Nonnull
    @Override
    public Shot map(@Nonnull final ShotRecord record)
    {
        return new Shot.Builder().coordinate(record.getX(), record.getY()).on(record.getOrdinal()).build();
    }
}
