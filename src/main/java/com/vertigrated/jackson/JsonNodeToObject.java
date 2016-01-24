package com.vertigrated.jackson;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public class JsonNodeToObject<T> implements Function<JsonNode, T>
{
    private final ObjectCodec codec;
    private final TypeReference<T> typeReference = new TypeReference<T>() {};

    public JsonNodeToObject(@Nonnull final ObjectCodec codec) { this.codec = codec; }

    @Nonnull @Override public T apply(@Nullable final JsonNode input)
    {
        try
        {
            return codec.treeAsTokens(input).readValueAs(typeReference);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
