package com.vertigrated.sitd.guice;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.vertigrated.sitd.representation.Shot;

public class DomainModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        requestStaticInjection(Shot.class);
        install(new FactoryModuleBuilder().build(Shot.class));
    }
}
