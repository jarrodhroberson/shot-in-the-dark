package com.vertigrated.sitd.auth;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.hash.Hashing;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vertigrated.sitd.representation.Proctor;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.basic.BasicCredentials;

import javax.annotation.Nonnull;

import static com.google.common.base.Charsets.UTF_8;

public class JsonProctorAuthenticator implements ProctorAuthenticator
{
    private final Provider<Cache<String,String>> credentialsProvider;

    @Inject
    JsonProctorAuthenticator(@Nonnull final Provider<Cache<String,String>> credentialsProvider)
    {
        this.credentialsProvider = credentialsProvider;
    }

    @Inject


    @Override
    public Optional<Proctor> authenticate(final BasicCredentials credentials) throws AuthenticationException
    {
        final String passwordHash = this.credentialsProvider.get().getIfPresent(credentials.getUsername());
        if (passwordHash == null)
        {
           return Optional.absent();
        }
        else if (passwordHash.equals(Hashing.sha1().hashString(credentials.getPassword(), UTF_8).toString()))
        {
            return Optional.of(new Proctor(credentials.getUsername()));
        }
        else
        {
            return Optional.absent();
        }
    }
}
