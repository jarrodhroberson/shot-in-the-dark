package com.vertigrated.sitd.service;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.vertigrated.sitd.guice.DerbyDataSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.inject.Scopes.SINGLETON;
import static java.lang.String.format;

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
            final Path db = Paths.get(new URI(new URI(this.dsf.getUrl()).getSchemeSpecificPart()).getSchemeSpecificPart().replace(";create=true", ""));
            if (Files.exists(db))
            {
                L.debug("Found existing database at {}", db.toAbsolutePath());
            }
            else
            {
                throw new RuntimeException(format("No database found at %s!, Please run the command \"db migrate <config.yml>\" to create the database!", db.toAbsolutePath()));
            }
        }
        catch (final URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Provider<DataSource> getDataSourceProvider()
    {
        try
        {
            final EmbeddedDataSource embeddedDataSource = new EmbeddedDataSource();
            embeddedDataSource.setDatabaseName(new URI(new URI(this.dsf.getUrl()).getSchemeSpecificPart()).getSchemeSpecificPart());
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
