package com.vertigrated.sitd.resource;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.vertigrated.sitd.representation.Board;
import com.vertigrated.sitd.representation.Game;
import com.vertigrated.sitd.representation.Shot;
import com.vertigrated.sitd.service.BoardService;
import com.vertigrated.sitd.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/game")
@Api("Game")
public class GameResource implements Resource
{
    private final GameService gameService;
    private final BoardService boardService;

    @Inject
    GameResource(@Nonnull final GameService gameService, @Nonnull final BoardService boardService)
    {
        this.gameService = gameService;
        this.boardService = boardService;
    }

    @GET
    @Path("/{player:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/{board:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    @ApiOperation("Create a New Game for a Player using a Board")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Game newGame(@Nonnull @PathParam("player") final UUID player, @Nonnull final @PathParam("board") UUID board)
    {
        return this.gameService.create(player, board);
    }

    @GET
    @Path("/{uuid:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    @ApiOperation("Get an existing Game by Id")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Game get(@Nonnull @PathParam("uuid") final UUID game)
    {
        return this.gameService.byId(game);
    }

    @GET
    @Path("/shoot/{game:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/{x:\\d+}/{y:\\d+}")
    @ApiOperation("Shoot at a Coordinate in a Game")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Response shoot(@Nonnull @PathParam("game") final UUID game, @Nonnull @PathParam("x") Integer x, @Nonnull @PathParam("y") Integer y)
    {
        final Game g = this.gameService.byId(game);
        final Board b = this.boardService.retrieve(g.board);
        this.gameService.recordShot(game, new Shot.Builder().coordinate(x, y).on(new Date()).build());
        if (b.test(x,y))
        {
            if (this.gameService.isComplete(game))
            {
                return Response.status(OK).entity(this.gameService.shotsByGame(g.id)).build();
            }
            else
            {
                return Response.status(NO_CONTENT).build();
            }
        }
        else
        {
            return Response.status(NOT_FOUND).build();
        }
    }

}
