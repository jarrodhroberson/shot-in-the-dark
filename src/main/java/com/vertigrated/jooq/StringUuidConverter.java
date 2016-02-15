package com.vertigrated.jooq;

import java.util.UUID;

import org.jooq.Converter;

public class StringUuidConverter implements Converter<String,UUID>
{
    @Override
    public UUID from(final String databaseObject)
    {
        return UUID.fromString(databaseObject);
    }

    @Override
    public String to(final UUID userObject)
    {
        return userObject.toString();
    }

    @Override
    public Class<String> fromType()
    {
        return String.class;
    }

    @Override
    public Class<UUID> toType()
    {
        return UUID.class;
    }
}
