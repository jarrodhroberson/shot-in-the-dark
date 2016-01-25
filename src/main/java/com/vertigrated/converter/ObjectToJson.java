package com.vertigrated.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Converter;
import com.google.common.reflect.TypeToken;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;

public class ObjectToJson<T> extends Converter<T, String>
{
    private final ObjectMapper om;
    private final Class<T> cls;

    @Inject
    @SuppressWarnings("unchecked")
    ObjectToJson(@Nonnull final ObjectMapper om)
    {
        this.om = om;
        this.cls = (Class<T>) new TypeToken<T>(getClass()) {}.getRawType();
    }

    @Override
    protected String doForward(@Nonnull final T o)
    {
        try { return om.writeValueAsString(o); }
        catch (final JsonProcessingException e) { throw new RuntimeException(e); }
    }

    @Override
    protected T doBackward(@Nonnull final String s)
    {
        try { return this.om.readValue(s, this.cls); }
        catch (final IOException e) { throw new RuntimeException(e); }
    }
}
