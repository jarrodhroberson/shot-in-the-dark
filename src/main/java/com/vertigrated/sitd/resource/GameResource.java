package com.vertigrated.sitd.resource;

import com.google.inject.Inject;
import com.vertigrated.sitd.Game;
import com.vertigrated.sitd.service.GameService;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.UUID;

@Path("/game")
public class GameResource implements Resource
{
    private final GameService gameService;

    @Inject
    GameResource(@Nonnull final GameService gameService)
    {
        this.gameService = gameService;
    }

    @GET
    @Path("/{player}/{board}")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Game newGame(@Nonnull @PathParam("player") final UUID player, @Nonnull final @PathParam("board") UUID board)
    {
        return this.gameService.create(player,board);
    }

    @GET
    @Path("/{uuid}")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Game get(@Nonnull @PathParam("uuid") final UUID game)
    {
        return this.gameService.retrieve(game);
    }

}
