package com.vertigrated.sitd.resource;

import javax.annotation.Nonnull;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@Path("/admin")
@Api(value = "Administration", authorizations = {@Authorization("admin")})
@Produces({"application/json", "text/xml", "text/csv", "text/html"})
public class AdminResource
{
    @POST
    @RolesAllowed({"admin","proctor"})
    @Path("/enable/{feature}/{player}")
    @ApiOperation("Enabled a feature for a Player by Id")
    @Consumes({"application/json", "text/xml", "text/csv"})
    @Produces({"application/json", "text/xml", "text/csv", "text/html"})
    public Response enable(@Nonnull @PathParam("feature") final String feature, @Nonnull @PathParam("player") final UUID player)
    {
        return null;
    }
}
