package com.vertigrated.sitd.service;

import com.vertigrated.sitd.Player;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface PlayerService
{
    public Player create(@Nonnull final String name);
    public Player retrieve(@Nonnull final String name);
    public Player retrieve(@Nonnull final UUID uuid);
    public Player all();
}
