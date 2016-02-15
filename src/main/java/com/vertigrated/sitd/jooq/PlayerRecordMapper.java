package com.vertigrated.sitd.jooq;

import com.vertigrated.sitd.jooq.tables.records.PlayerRecord;
import com.vertigrated.sitd.representation.Player;
import org.jooq.RecordMapper;

import javax.annotation.Nonnull;
import java.util.UUID;

public class PlayerRecordMapper implements RecordMapper<PlayerRecord,Player>
{
    @Nonnull
    @Override
    public Player map(@Nonnull final PlayerRecord record)
    {
        return new Player.Builder().player(UUID.fromString(record.getId())).name(record.getName()).build();
    }
}
