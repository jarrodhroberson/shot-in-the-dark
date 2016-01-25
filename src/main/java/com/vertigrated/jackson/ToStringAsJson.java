package com.vertigrated.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.annotation.Nonnull;

@Singleton
public class ToStringAsJson implements MethodInterceptor
{
    private final Provider<ObjectMapper> omp;

    @Inject
    public ToStringAsJson(@Nonnull final Provider<ObjectMapper> objectMapperProvider)
    {
        this.omp = objectMapperProvider;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable
    {
        return this.omp.get().writeValueAsString(invocation.getThis());
    }
}
