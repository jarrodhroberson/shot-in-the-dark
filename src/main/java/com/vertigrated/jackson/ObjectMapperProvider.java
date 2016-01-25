package com.vertigrated.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Provider;

import javax.annotation.Nonnull;

public class ObjectMapperProvider implements Provider<ObjectMapper>
{
    private final ObjectMapper om;

    public ObjectMapperProvider(@Nonnull final ObjectMapper om)
    {
        this.om = om;
    }

    @Override
    public ObjectMapper get() { return this.om; }
}
