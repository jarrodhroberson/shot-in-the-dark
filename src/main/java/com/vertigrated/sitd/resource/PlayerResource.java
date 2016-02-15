package com.vertigrated.sitd.resource;

import com.google.inject.Inject;
import com.vertigrated.sitd.representation.Player;
import com.vertigrated.sitd.service.PlayerService;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.UUID;

@Path("/player")
public class PlayerResource
{
    private final PlayerService playerService;

    @Inject
    PlayerResource(@Nonnull final PlayerService playerService)
    {
        this.playerService = playerService;
    }

    @GET
    @Path("/{uuid}")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Player get(@Nonnull @PathParam("uuid") final UUID uuid)
    {
        return this.playerService.retrieve(uuid);
    }

    @GET
    @Path("/register/{gmid}")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Player register(@Nonnull @PathParam("gmid") final String gmid)
    {
        return this.playerService.create(gmid);
    }
}
