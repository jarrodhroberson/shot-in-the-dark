package com.vertigrated.sitd.service;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.vertigrated.sitd.guice.DerbyDataSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.inject.Scopes.SINGLETON;

public class DerbyDatabaseServiceModule extends AbstractModule
{
    private static final Logger L = LoggerFactory.getLogger(DerbyDatabaseServiceModule.class);

    private final DataSourceFactory dsf;

    public DerbyDatabaseServiceModule(@Nonnull final DataSourceFactory dataSourceFactory)
    {
        this.dsf = dataSourceFactory;
        this.ensureDatabaseExists();
    }

    private void ensureDatabaseExists()
    {
        try
        {
            final Path db = Paths.get(new URI(new URI(this.dsf.getUrl()).getSchemeSpecificPart().replace(";create=true","")).getPath());
            if (Files.exists(db))
            {
                L.debug("Found existing database at {}", db.toAbsolutePath());
            }
            else
            {
                final List<String> sql = Lists.transform(Arrays.asList(Resources.toString(Resources.getResource("derby/create_tables.sql"), UTF_8).split(";")), new Function<String, String>() {
                    @Nullable
                    @Override
                    public String apply(@SuppressWarnings("NullableProblems") @Nonnull final String input)
                    {
                        return input + "\n";
                    }
                });
                this.executeBatch(sql);
            }
        }
        catch (URISyntaxException | IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void executeBatch(final List<String> sql)
    {
        final Connection cn;
        try
        {
            cn = this.getDataSourceProvider().get().getConnection(this.dsf.getUser(), this.dsf.getPassword());
            try
            {
                cn.setAutoCommit(false); // always set autocommit to false on batch operations
                final Statement stmt = cn.createStatement();
                try
                {
                    for (final String s : sql)
                    {
                        L.info(s);
                        stmt.addBatch(s);
                    }
                    stmt.executeBatch();
                    cn.commit();
                }
                catch (final SQLException e)
                {
                    cn.rollback();
                    throw new RuntimeException(e);
                }
                finally
                {
                    try { stmt.close(); } catch (final SQLException e) { L.error(e.getMessage(), e); }
                }
            }
            finally
            {
                try { cn.close(); } catch (final SQLException e) { L.error(e.getMessage(), e); }
            }
        }
        catch (final SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Provider<DataSource> getDataSourceProvider()
    {
        try
        {
            final EmbeddedDataSource embeddedDataSource = new EmbeddedDataSource();
            embeddedDataSource.setDatabaseName(new URI(new URI(this.dsf.getUrl()).getSchemeSpecificPart()).getPath());
            embeddedDataSource.setUser(this.dsf.getUser());
            embeddedDataSource.setPassword(this.dsf.getPassword());
            return new DerbyDataSourceProvider(embeddedDataSource);
        }
        catch (final URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void configure()
    {
        bind(DataSource.class).toProvider(this.getDataSourceProvider()).in(SINGLETON);
        bind(GameService.class).to(DatabaseGameService.class).in(SINGLETON);
        bind(BoardService.class).to(DatabaseBoardService.class).in(SINGLETON);
        bind(PlayerService.class).to(DatabasePlayerService.class).in(SINGLETON);
    }
}
