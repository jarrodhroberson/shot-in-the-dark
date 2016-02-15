package com.vertigrated.sitd.auth;

import com.google.common.base.Optional;
import com.google.common.hash.Hashing;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vertigrated.sitd.representation.Player;
import com.vertigrated.sitd.jooq.PlayerRecordMapper;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.basic.BasicCredentials;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

import static com.google.common.base.Charsets.UTF_8;
import static com.vertigrated.sitd.jooq.Tables.PLAYER;
import static com.vertigrated.sitd.jooq.Tables.PRINCIPAL;

public class DatabasePlayerAuthenticator implements PlayerAuthenticator
{
    private final Provider<DataSource> dataSourceProvider;

    @Inject
    DatabasePlayerAuthenticator(@Nonnull final Provider<DataSource> dataSourceProvider)
    {
        this.dataSourceProvider = dataSourceProvider;
    }

    @Override
    public Optional<Player> authenticate(final BasicCredentials credentials) throws AuthenticationException
    {
        final DSLContext dsl = DSL.using(this.dataSourceProvider.get(), SQLDialect.DERBY);
            return Optional.of(dsl.selectFrom(PLAYER)
                                  .where(PLAYER.NAME.equal(credentials.getUsername()))
                                  .and(PRINCIPAL.ID.equal(PLAYER.ID))
                                  .and(PRINCIPAL.PASSWORD.equal(Hashing.sha1().hashString(credentials.getPassword(), UTF_8).toString()))
                              .fetchOne(new PlayerRecordMapper()));
    }
}
