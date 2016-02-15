package com.vertigrated.sitd.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vertigrated.sitd.jooq.PlayerRecordMapper;
import com.vertigrated.sitd.jooq.Tables;
import com.vertigrated.sitd.jooq.tables.records.PlayerRecord;
import com.vertigrated.sitd.representation.Player;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.util.UUID;

public class DatabasePlayerService extends DatabaseService implements PlayerService
{
    private static final Logger L = LoggerFactory.getLogger(DatabasePlayerService.class);

    private final com.vertigrated.sitd.jooq.tables.Player PLAYER = Tables.PLAYER;

    @Inject
    DatabasePlayerService(@Nonnull final Provider<DataSource> dataSourceProvider)
    {
        super(dataSourceProvider);
    }

    @Override
    public Player create(@Nonnull final String name)
    {
        L.debug("Creating Player {}", name);
        final Player p = new Player.Builder().player(UUID.randomUUID()).name(name).build();
        final DSLContext c = DSL.using(super.connection());
        c.insertInto(PLAYER).set(PLAYER.ID, p.id.toString()).set(PLAYER.NAME, name).execute();
        return p;
    }

    @Override
    public Player retrieve(@Nonnull final String name)
    {
        final DSLContext c = DSL.using(super.connection());
        return c.selectFrom(PLAYER).where(PLAYER.NAME.equal(name)).fetchOne(new PlayerRecordMapper());
    }

    @Override
    public Player retrieve(@Nonnull final UUID uuid)
    {
        final DSLContext c = DSL.using(super.connection());
        final PlayerRecord pr = c.selectFrom(PLAYER).where(PLAYER.ID.equal(uuid.toString())).fetchOne();
        return new Player.Builder().player(UUID.fromString(pr.getId())).name(pr.getName()).build();
    }

    @Override
    public Player all()
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.DatabasePlayerService.all()");
    }
}
