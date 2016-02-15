package com.vertigrated.sitd.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.vertigrated.sitd.representation.Proctor;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class ProctorCredentialsProvider implements Provider<Cache<String, Proctor>>
{
    private final Cache<String, Proctor> cache;
    private final WatchService watchService;

    @Inject
    ProctorCredentialsProvider(@Nonnull final Path path)
    {
        try
        {
            this.watchService = FileSystems.getDefault().newWatchService();
            path.getParent().register(this.watchService, ENTRY_MODIFY);
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    while (true)
                    {
                        try
                        {
                            final WatchKey wk = watchService.take();
                            for (final WatchEvent<?> we : wk.pollEvents())
                            {
                                if (we.kind() == ENTRY_MODIFY && path.getFileName().equals(we.context()))
                                {
                                    cache.invalidateAll();
                                    wk.reset();
                                    break;
                                }
                            }
                        }
                        catch (final InterruptedException e) { throw new RuntimeException(e); }
                    }
                }
            }, "ProctorCredentialsWatchService").start();
        }
        catch (final IOException e) { throw new RuntimeException(e); }
        this.cache = CacheBuilder.newBuilder()
                                 .maximumSize(100)
                                 .expireAfterAccess(3, TimeUnit.MINUTES)
                                 .build(new CacheLoader<String, Proctor>()
                                 {
                                     @Override
                                     public Proctor load(@Nonnull final String key) throws Exception
                                     {
                                         return this.loadAll(Lists.newArrayList(key)).get(key);
                                     }

                                     @Override
                                     public Map<String, Proctor> loadAll(final Iterable<? extends String> keys) throws Exception
                                     {
                                         return new ObjectMapper().readValue(Files.newInputStream(path, StandardOpenOption.READ), new TypeReference<Map<String, Proctor>>() {});
                                     }
                                 });
    }

    @Override
    public Cache<String, Proctor> get()
    {
        return this.cache;
    }
}
