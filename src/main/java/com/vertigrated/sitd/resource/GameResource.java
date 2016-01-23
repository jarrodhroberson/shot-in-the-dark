package com.vertigrated.sitd.resource;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/game")
public class GameResource implements Resource
{
    @GET
    @Path("/{user}/{board}")
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Response startGame(@Nonnull @PathParam("user") final String user)
    {
        return null;
    }
}
