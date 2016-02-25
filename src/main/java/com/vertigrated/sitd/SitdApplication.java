package com.vertigrated.sitd;

import com.fasterxml.jackson.annotation.JsonInclude;
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
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.paradoxical.dropwizard.swagger.AdminResourceConfigurator;
import io.paradoxical.dropwizard.swagger.SwaggerAssetsBundle;
import io.swagger.jaxrs.config.BeanConfig;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class SitdApplication extends Application<SitdConfiguration>
{
    private final AdminResourceConfigurator adminResourceConfigurator = AdminResourceConfigurator.builder().adminRootPath("/admin").build();

    @Override
    public void initialize(@Nonnull final Bootstrap<SitdConfiguration> bootstrap)
    {
        bootstrap.addBundle(new ViewBundle<>());
        bootstrap.addBundle(new MigrationsBundle<SitdConfiguration>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(final SitdConfiguration configuration)
            {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(new SwaggerAssetsBundle(new Function<Environment, BeanConfig>()
        {
            @Override
            public BeanConfig apply(final Environment environment)
            {
                final BeanConfig config = new BeanConfig();
                config.setTitle("Shot In The Dark");
                config.setVersion("1.0.0");
                config.setResourcePackage("com.vertigrated.sitd.resource");
                config.setScan(true);
                return config;
            }
        }));
    }

    @Override
    public void run(@Nonnull final SitdConfiguration serviceConfiguration, @Nonnull final Environment environment) throws Exception
    {
//        this.adminResourceConfigurator.enableSwagger(environment, this.getAdminSwaggerScanner());
//        this.adminResourceConfigurator.getAdminResourceConfig().register(AdminResource.class);
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
        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private BeanConfig getAdminSwaggerScanner()
    {
        final BeanConfig config = new BeanConfig();
        config.setTitle("Admin API");
        config.setDescription("Admin API");
        config.setResourcePackage(AdminResource.class.getPackage().getName());
        config.setContact("admin@site.com");
        config.setPrettyPrint(true);
        config.setVersion("1.0.0");
        config.setBasePath("/admin");
        return config;
    }

    public static void main(final String[] args) throws Exception
    {
        new SitdApplication().run(args);
    }
}
