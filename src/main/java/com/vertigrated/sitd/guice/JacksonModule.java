package com.vertigrated.sitd.guice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.vertigrated.sitd.representation.Shot;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;

public class JacksonModule extends AbstractModule
{
    private final ObjectMapper HUMAN_FRIENDLY = new ObjectMapper().enable(INDENT_OUTPUT);
    private final ObjectMapper COMPACT = new ObjectMapper();

    @Override
    protected void configure()
    {
        bind(ObjectMapper.class).toProvider(new Provider<ObjectMapper>() {
            @Override
            public ObjectMapper get()
            {
                return COMPACT;
            }
        });

        install(new FactoryModuleBuilder().build(Shot.ShotFactory.class));
    }
}
