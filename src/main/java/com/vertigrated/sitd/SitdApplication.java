package com.vertigrated.sitd;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vertigrated.sitd.auth.DatabasePlayerAuthenticator;
import com.vertigrated.sitd.auth.PlayerAuthorizer;
import com.vertigrated.sitd.representation.Player;
import com.vertigrated.sitd.resource.*;
import com.vertigrated.sitd.service.DerbyDatabaseServiceModule;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.annotation.Nonnull;

public class SitdApplication extends Application<SitdConfiguration>
{
    @Override
    public void initialize(@Nonnull final Bootstrap<SitdConfiguration> bootstrap)
    {

    }

    @Override
    public void run(@Nonnull final SitdConfiguration serviceConfiguration, @Nonnull final Environment environment) throws Exception
    {
        final Injector injector = Guice.createInjector(new SitdModule(serviceConfiguration),
                new DerbyDatabaseServiceModule(serviceConfiguration.getDataSourceFactory()));

        environment.jersey().register(injector.getInstance(BoardResource.class));
        environment.jersey().register(injector.getInstance(GameResource.class));
        environment.jersey().register(injector.getInstance(PlayerResource.class));
        environment.jersey().register(injector.getInstance(ReportResource.class));
        environment.jersey().register(injector.getInstance(AdminResource.class));
        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<Player>()
                                                                     .setAuthenticator(injector.getInstance(DatabasePlayerAuthenticator.class))
                                                                     .setAuthorizer(injector.getInstance(PlayerAuthorizer.class))
                                                                     .setRealm("PLAYER")
                                                                     .buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(Player.class));
    }

    public static void main(final String[] args) throws Exception
    {
        new SitdApplication().run(args);
    }
}
