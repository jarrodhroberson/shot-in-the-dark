package com.vertigrated.sitd.service;

import com.vertigrated.sitd.representation.Board;
import com.vertigrated.sitd.representation.Game;
import com.vertigrated.sitd.representation.Shot;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface GameService
{
    public Game create(@Nonnull final UUID user, @Nonnull final UUID board);
    public Set<UUID> byPlayerBoard(@Nonnull final UUID user, @Nonnull final UUID board);
    public Game byId(@Nonnull final UUID game);
    public Set<Game> byIds(@Nonnull final Collection<UUID> games);
    public Set<UUID> byPlayer(@Nonnull final UUID player);
    public void recordShot(@Nonnull final UUID game, @Nonnull final Shot shot);
    public List<Shot> shotsByGame(@Nonnull final UUID game);
    public Set<UUID> all();
    public boolean isComplete(@Nonnull final UUID game);
}
