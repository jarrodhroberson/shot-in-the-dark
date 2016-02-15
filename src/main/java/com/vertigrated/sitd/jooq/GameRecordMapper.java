package com.vertigrated.sitd.jooq;

import java.util.UUID;

import com.vertigrated.sitd.jooq.tables.records.GameRecord;
import com.vertigrated.sitd.representation.Game;
import org.jooq.RecordMapper;

public class GameRecordMapper implements RecordMapper<GameRecord,Game>
{
    @Override
    public Game map(final GameRecord record)
    {
        return new Game.Builder().id(UUID.fromString(record.getId()))
                .player(UUID.fromString(record.getPlayer()))
                .board(UUID.fromString(record.getBoard()))
                .build();
    }
}
