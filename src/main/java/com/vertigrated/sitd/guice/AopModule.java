package com.vertigrated.sitd.guice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.vertigrated.jackson.ToJson;
import com.vertigrated.jackson.ToStringAsJson;

public class AopModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(ToJson.class), new ToStringAsJson(getProvider(ObjectMapper.class)));
    }
}
