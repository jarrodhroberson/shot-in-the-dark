package com.vertigrated.sitd.resource;

import com.google.inject.Inject;
import com.vertigrated.sitd.Target;
import com.vertigrated.sitd.board.Board;
import com.vertigrated.sitd.board.RandomTargetPlacementStrategy;
import com.vertigrated.sitd.board.SetTargetPlacementStrategy;
import com.vertigrated.sitd.service.BoardService;

import javax.annotation.Nonnull;
import javax.ws.rs.*;
import java.util.Set;
import java.util.UUID;

@Path("/board")
public class BoardResource implements Resource
{
    private final BoardService boardService;

    @Inject
    BoardResource(@Nonnull final BoardService boardService)
    {
        this.boardService = boardService;
    }

    @GET
    @Path("/{uuid}")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Board get(@Nonnull @PathParam("uuid") final UUID board)
    {
        return this.boardService.retrieve(board);
    }

    @GET
    @Path("/{width}/{height}/{minTargetSize}/{maxTargetSize}/{targetCount}")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public UUID newBoard(@Nonnull @PathParam("width") final Integer width, @Nonnull @PathParam("height") final Integer height, @Nonnull @PathParam("minTargetSize") final Integer minTargetSize,
                          @Nonnull @PathParam("maxTargetSize") final Integer maxTargetSize, @Nonnull @PathParam("targetCount") final Integer targetCount)
    {
        return this.boardService.create(width,height, new RandomTargetPlacementStrategy(minTargetSize, maxTargetSize, targetCount)).id;
    }

    @POST
    @Path("/{width}/{height}")
    @Consumes({"application/json", "text/xml", "text/csv"})
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public UUID newBoard(@Nonnull @PathParam("width") final Integer width, @Nonnull @PathParam("height") final Integer height, final Set<Target> targets)
    {
        return this.boardService.create(width,height, new SetTargetPlacementStrategy(targets)).id;
    }
}
