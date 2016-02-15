package com.vertigrated.sitd.jooq;

import com.vertigrated.sitd.jooq.tables.records.TargetRecord;
import com.vertigrated.sitd.representation.Coordinate;
import com.vertigrated.sitd.representation.Coordinates;
import com.vertigrated.sitd.representation.Target;
import org.jooq.RecordMapper;

import javax.annotation.Nonnull;

public class TargetRecordMapper implements RecordMapper<TargetRecord,Target>
{

    @Nonnull
    @Override
    public Target map(@Nonnull final TargetRecord record)
    {
        return new Target(new Coordinates.Builder().start(new Coordinate(record.getStartX(),record.getStartY())).end(new Coordinate(record.getEndX(),record.getEndY())).build());
    }
}
