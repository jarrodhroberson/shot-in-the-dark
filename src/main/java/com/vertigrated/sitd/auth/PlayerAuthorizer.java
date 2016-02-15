package com.vertigrated.sitd.auth;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vertigrated.sitd.representation.Player;
import io.dropwizard.auth.Authorizer;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

import static com.vertigrated.sitd.jooq.Tables.PRINCIPAL_ROLE;

public class PlayerAuthorizer implements Authorizer<Player>
{
    private final Provider<DataSource> dataSourceProvider;

    @Inject
    PlayerAuthorizer(@Nonnull final Provider<DataSource> dataSourceProvider)
    {
        this.dataSourceProvider = dataSourceProvider;
    }

    @Override
    public boolean authorize(final Player principal, final String role)
    {
        final DSLContext dsl = DSL.using(this.dataSourceProvider.get(), SQLDialect.DERBY);
        return dsl.selectCount().where(PRINCIPAL_ROLE.PRINCIPAL.equal(principal.id.toString())).fetchOne(0,int.class) > 0;
    }
}
