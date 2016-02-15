package com.vertigrated.sitd;

import com.google.inject.AbstractModule;
import com.vertigrated.sitd.auth.InMemoryProctorAuthenticator;
import com.vertigrated.sitd.auth.ProctorAuthenticator;
import com.vertigrated.sitd.guice.*;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.StandardSystemProperty.USER_DIR;
import static com.google.common.base.StandardSystemProperty.USER_HOME;
import static com.google.common.base.StandardSystemProperty.USER_NAME;

public class SitdModule extends AbstractModule
{
    private final SitdConfiguration configuration;

    public SitdModule(@Nonnull final SitdConfiguration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    protected void configure()
    {
        bind(Path.class).annotatedWith(CurrentDirectory.class).toInstance(Paths.get(USER_DIR.value()));
        bind(Path.class).annotatedWith(CurrentUserHomeDirectory.class).toInstance(Paths.get(USER_HOME.value()));
        bind(String.class).annotatedWith(CurrentUser.class).toInstance(USER_NAME.value());
        bind(Path.class).annotatedWith(PathToProctorCredentials.class).toInstance(Paths.get(this.configuration.getPathToProctorCredentials()));
        bind(ProctorAuthenticator.class).toInstance(new InMemoryProctorAuthenticator());
    }
}
