package com.vertigrated.sitd.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Provider;
import com.vertigrated.sitd.board.Board;
import com.vertigrated.sitd.Game;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.Connection;
import java.util.UUID;

import static org.jooq.SQLDialect.DERBY;

public class DatabaseGameService implements GameService
{
    private static final Logger L = LoggerFactory.getLogger(DatabaseGameService.class);

    private final Provider<Connection> connectionProvider;
    private final ObjectMapper objectMapper;

    @Inject
    DatabaseGameService(@Nonnull final Provider<Connection> connectionProvider, @Nonnull final ObjectMapper objectMapper)
    {
        this.connectionProvider = connectionProvider;
        this.objectMapper = objectMapper;
    }

    public Game create(@Nonnull final UUID user, @Nonnull final Board board)
    {
        final DSLContext create = DSL.using(this.connectionProvider.get(), DERBY);
        return null;
    }

    @Override
    public Game create(@Nonnull final UUID user, @Nonnull final UUID board)
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.DatabaseGameService.create()");
    }

    @Override public Game retrieve(@Nonnull final UUID game)
    {
        throw new UnsupportedOperationException("com.vertigrated.sitd.service.DatabaseGameService.retrieve()");
    }
}
