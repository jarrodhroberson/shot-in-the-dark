package com.vertigrated.sitd.resource;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/board")
public class BoardResource implements Resource
{
    @GET
    @Path("/{uuid}")
    @Produces({"application/json","text/xml","text/csv","text/html"})
    public Response getBoard(@Nonnull @PathParam("uuid") final UUID uuid)
    {
        return null;
    }
}
