package com.vertigrated.sitd.resource;

import com.google.inject.Inject;
import com.vertigrated.sitd.representation.Player;
import com.vertigrated.sitd.service.PlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.Set;
import java.util.UUID;

@Path("/player")
@Api("Player")
@Produces({"application/json", "text/xml", "text/csv", "text/html"})
public class PlayerResource
{
    private final PlayerService playerService;

    @Inject
    PlayerResource(@Nonnull final PlayerService playerService)
    {
        this.playerService = playerService;
    }

    @GET
    @Path("/{uuid:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    @ApiOperation("Get Player by Id")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Player get(@Nonnull @PathParam("uuid") final UUID uuid)
    {
        return this.playerService.byId(uuid);
    }

    @GET
    @Path("/register/{gmid:[a-zA-Z0-9]{6}}")
    @ApiOperation("Register Player GMID")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Player register(@Nonnull @PathParam("gmid") final String gmid)
    {
        return this.playerService.create(gmid);
    }

    @GET
    @Path("/all")
    @ApiOperation("Get All Players")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Set<Player> all()
    {
        return this.playerService.all();
    }
}
