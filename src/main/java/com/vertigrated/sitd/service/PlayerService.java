package com.vertigrated.sitd.service;

import com.vertigrated.sitd.representation.Player;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

public interface PlayerService
{
    public Player create(@Nonnull final String name);
    public Player byName(@Nonnull final String name);
    public Player byId(@Nonnull final UUID uuid);
    public Set<Player> all();
}
