package com.vertigrated.sitd.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.vertigrated.sitd.representation.Shot;

public class FactoryModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        install(new FactoryModuleBuilder().build(Shot.ShotFactory.class));
        requestStaticInjection(Shot.class);
    }
}
