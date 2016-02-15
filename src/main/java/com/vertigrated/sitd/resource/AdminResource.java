package com.vertigrated.sitd.resource;

import javax.annotation.Nonnull;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/admin")
public class AdminResource
{
    @POST
    @RolesAllowed({"admin","proctor"})
    @Path("/enable/{feature}/{player}")
    @Consumes({"application/json", "text/xml", "text/csv"})
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Response enable(@Nonnull @PathParam("feature") final String feature, @Nonnull @PathParam("player") final UUID player)
    {
        return null;
    }
}
