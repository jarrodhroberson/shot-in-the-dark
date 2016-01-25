package com.vertigrated.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Converter;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class JsonNodeToList<T> extends Converter<JsonNode,List<T>>
{
    @SuppressWarnings("unchecked")
    private final Class<T> cls = (Class<T>) new TypeToken<T>(getClass()) {}.getRawType();
    private final ObjectMapper om;

    @Inject
    JsonNodeToList(@Nonnull final ObjectMapper om)
    {
        this.om = om;
    }

    @Nonnull
    @Override
    protected List<T> doForward(@Nonnull final JsonNode jsonNode)
    {
        final ImmutableList.Builder<T> ilb = ImmutableList.builder();
        if (checkNotNull(jsonNode).isArray())
        {
            for (final JsonNode jn : jsonNode)
            {
                try { ilb.add(this.om.treeToValue(jn, this.cls)); }
                catch (JsonProcessingException e) { throw new RuntimeException(e); }

            }
        }
        return ilb.build();
    }

    @Nonnull
    @Override
    protected JsonNode doBackward(@Nonnull final List<T> list) { return this.om.valueToTree(list); }
}
