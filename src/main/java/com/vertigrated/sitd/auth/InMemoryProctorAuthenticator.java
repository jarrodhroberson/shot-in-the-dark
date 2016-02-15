package com.vertigrated.sitd.auth;

import com.google.common.base.Optional;
import com.vertigrated.sitd.representation.Proctor;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.basic.BasicCredentials;

public class InMemoryProctorAuthenticator implements ProctorAuthenticator
{
    @Override
    public Optional<Proctor> authenticate(final BasicCredentials credentials) throws AuthenticationException
    {
        return Optional.of(new Proctor("default"));
    }
}
