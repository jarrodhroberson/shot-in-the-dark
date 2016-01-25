package com.vertigrated.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Converter;
import com.google.inject.Inject;

import javax.annotation.Nonnull;
import java.io.IOException;

public class JsonNodeToObject<T> extends Converter<JsonNode, T>
{
    private final TypeReference<T> typeReference = new TypeReference<T>() {};
    private final ObjectMapper om;

    @Inject
    JsonNodeToObject(@Nonnull final ObjectMapper om) { this.om = om; }

    @Nonnull
    @Override
    protected T doForward(@Nonnull final JsonNode jsonNode)
    {
        try { return this.om.treeAsTokens(jsonNode).readValueAs(typeReference); }
        catch (final IOException e) { throw new RuntimeException(e); }
    }

    @Nonnull
    @Override
    protected JsonNode doBackward(@Nonnull final T t) { return this.om.valueToTree(t); }
}
