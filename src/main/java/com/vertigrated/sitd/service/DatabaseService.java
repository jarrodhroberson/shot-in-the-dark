package com.vertigrated.sitd.service;

import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseService
{
    protected final Provider<DataSource> dataSourceProvider;

    @Inject
    protected DatabaseService(@Nonnull final Provider<DataSource> dataSourceProvider)
    {
        this.dataSourceProvider = dataSourceProvider;
    }

    protected Connection connection()
    {
        try
        {
            return this.dataSourceProvider.get().getConnection();
        }
        catch (final SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}
