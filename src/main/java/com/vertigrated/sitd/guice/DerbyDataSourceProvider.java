package com.vertigrated.sitd.guice;

import com.google.inject.Provider;
import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.annotation.Nonnull;
import javax.sql.DataSource;

public class DerbyDataSourceProvider implements Provider<DataSource>
{
    private final EmbeddedDataSource embeddedDataSource;

    public DerbyDataSourceProvider(@Nonnull final EmbeddedDataSource embeddedDataSource)
    {
        this.embeddedDataSource = embeddedDataSource;
    }

    @Override
    public DataSource get()
    {
        return this.embeddedDataSource;
    }
}
