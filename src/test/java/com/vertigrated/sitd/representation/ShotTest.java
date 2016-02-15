package com.vertigrated.sitd.representation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.vertigrated.sitd.guice.AopModule;
import com.vertigrated.sitd.guice.FactoryModule;
import com.vertigrated.sitd.guice.JacksonModule;
import com.vertigrated.sitd.representation.Shot;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ShotTest
{
    private final Date placed = new Date(1453696633161L);
    @Inject
    private Injector injector;
    @Inject
    private Provider<ObjectMapper> omp;

    @Before
    public void setUp()
    {
        final Injector i = Guice.createInjector(new FactoryModule()
                , new JacksonModule(),
                new AopModule());
        i.injectMembers(this);
    }

    @Test
    public void testSerialization() throws Exception
    {
        final String json = this.omp.get().writeValueAsString(injector.getInstance(Shot.Builder.class).coordinate(0, 0).on(this.placed).build());
        assertThat("{\"coordinate\":{\"x\":0,\"y\":0},\"placed\":1453696633161}", is(equalTo(json)));
    }

    @Test
    public void testDeserialization() throws Exception
    {
        final Shot s = this.omp.get().readValue("{\"coordinate\":{\"x\":0,\"y\":0},\"placed\":1453696633161}", Shot.class);
        assertThat(new Shot.Builder().coordinate(0, 0).on(this.placed).build(), is(equalTo(s)));
    }
}
