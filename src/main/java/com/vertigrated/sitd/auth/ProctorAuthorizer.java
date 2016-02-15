package com.vertigrated.sitd.auth;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vertigrated.sitd.representation.Proctor;
import io.dropwizard.auth.Authorizer;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

public class ProctorAuthorizer implements Authorizer<Proctor>
{
    private final Provider<DataSource> dataSourceProvider;

    @Inject
    ProctorAuthorizer(@Nonnull final Provider<DataSource> dataSourceProvider)
    {
        this.dataSourceProvider = dataSourceProvider;
    }

    @Override
    public boolean authorize(final Proctor principal, final String role)
    {
        return true;
    }
}
