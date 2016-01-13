package com.vertigrated.sitd;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class ServiceApplication extends Application<ServiceConfiguration>
{
    public static void main(final String[] args) throws Exception
    {
        new ServiceApplication().run(args);
    }

    @Override public void run(final ServiceConfiguration serviceConfiguration, final Environment environment) throws Exception
    {

    }
}
