package com.vertigrated.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Converter;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class JsonNodeToSet<T> extends Converter<JsonNode,Set<T>>
{
    @SuppressWarnings("unchecked")
    private final Class<T> cls = (Class<T>) new TypeToken<T>(getClass()) {}.getRawType();
    private final ObjectMapper om;

    @Inject
    JsonNodeToSet(@Nonnull final ObjectMapper om)
    {
        this.om = om;
    }

    @Nonnull
    @Override
    protected Set<T> doForward(@Nonnull final JsonNode jsonNode)
    {
        final ImmutableSet.Builder<T> isb = ImmutableSet.builder();
        if (checkNotNull(jsonNode).isArray())
        {
            for (final JsonNode jn : jsonNode)
            {
                try { isb.add(this.om.treeToValue(jn, this.cls)); }
                catch (JsonProcessingException e) { throw new RuntimeException(e); }

            }
        }
        return isb.build();
    }

    @Nonnull
    @Override
    protected JsonNode doBackward(@Nonnull final Set<T> set) { return this.om.valueToTree(set); }
}
