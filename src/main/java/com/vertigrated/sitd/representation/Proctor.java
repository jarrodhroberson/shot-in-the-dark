package com.vertigrated.sitd.representation;

import javax.annotation.Nonnull;
import javax.security.auth.Subject;
import java.util.UUID;

import static com.google.common.base.Charsets.UTF_8;

public class Proctor implements java.security.Principal
{
    private final UUID id;
    private final String name;

    public Proctor(@Nonnull final String name)
    {
        this.name = name;
        this.id = UUID.nameUUIDFromBytes(this.name.getBytes(UTF_8));
    }

    public UUID getId()
    {
        return id;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public boolean implies(final Subject subject)
    {
        return true;
    }
}
