package com.vertigrated.sitd.resource;

import com.google.inject.Inject;
import com.vertigrated.sitd.representation.Board;
import com.vertigrated.sitd.representation.RandomTargetPlacementStrategy;
import com.vertigrated.sitd.service.BoardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.Set;
import java.util.UUID;

@Path("/board")
@Api("Board")
@Produces({"application/json", "text/xml", "text/csv", "text/html"})
public class BoardResource implements Resource
{
    private final BoardService boardService;

    @Inject
    BoardResource(@Nonnull final BoardService boardService)
    {
        this.boardService = boardService;
    }

    @GET
    @Path("/{uuid:[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    @ApiOperation("Get Board by Id")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Board get(@Nonnull @PathParam("uuid") final UUID board)
    {
        return this.boardService.retrieve(board);
    }

    @GET
    @Path("/all")
    @ApiOperation("Get all Boards")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Set<UUID> all()
    {
        return this.boardService.all();
    }

    @GET
    @Path("/{width}/{height}/{minTargetSize}/{maxTargetSize}/{targetCount}")
    @ApiOperation("Create New Board with Randomly Placed Targets")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public UUID newBoard(@Nonnull @PathParam("width") final Integer width, @Nonnull @PathParam("height") final Integer height, @Nonnull @PathParam("minTargetSize") final Integer minTargetSize,
                          @Nonnull @PathParam("maxTargetSize") final Integer maxTargetSize, @Nonnull @PathParam("targetCount") final Integer targetCount)
    {
        return this.boardService.create(width,height, new RandomTargetPlacementStrategy(minTargetSize, maxTargetSize, targetCount)).id;
    }

//    @POST
//    @Path("/{width:\\d+}/{height:\\d}")
//    @Consumes({"application/json", "text/xml", "text/csv"})
//    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
//    public UUID newBoard(@Nonnull @PathParam("width") final Integer width, @Nonnull @PathParam("height") final Integer height, final Set<Target> targets)
//    {
//        return this.boardService.create(width,height, new SetTargetPlacementStrategy(targets)).id;
//    }
}
