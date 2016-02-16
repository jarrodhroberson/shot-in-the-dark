package com.vertigrated.sitd.resource;

import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.google.inject.Inject;
import com.vertigrated.sitd.service.BoardService;
import com.vertigrated.sitd.service.GameService;
import com.vertigrated.sitd.service.PlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/report")
@Api("Reporting")
public class ReportResource
{
    private final PlayerService playerService;
    private final BoardService boardService;
    private final GameService gameService;

    @Inject
    ReportResource(@Nonnull final PlayerService playerService, @Nonnull final BoardService boardService, @Nonnull final GameService gameService)
    {
        this.playerService = playerService;
        this.boardService = boardService;
        this.gameService = gameService;
    }

    @GET
    @Path("/games/{player:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    @ApiOperation("Get a list of Games by Player")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Set<UUID> getGamesByPlayer(@Nonnull @PathParam("player") final UUID player)
    {
        return this.gameService.byPlayer(player);
    }
}
