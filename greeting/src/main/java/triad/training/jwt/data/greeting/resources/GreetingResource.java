package triad.training.jwt.data.greeting.resources;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Path("/greeting")
@RequestScoped
@Produces(MediaType.TEXT_PLAIN)
public class GreetingResource {

    private JsonWebToken jwt;

    @Inject
    public GreetingResource(JsonWebToken jsonWebToken) {
        this.jwt = jsonWebToken;
    }

    @GET
    @PermitAll
    @Path("/open")
    public String hello() {
        return "hello";
    }

    @GET
    @PermitAll
    @Path("/open-secinfo")
    public String secretHello(@Context SecurityContext ctx) {
        Principal caller = ctx.getUserPrincipal();
        String name = caller == null ? "anonymous" : caller.getName();
        boolean hasJWT = jwt.getClaimNames() != null;
        return String.format("Hello %s.\nisSecure: %s\nauthScheme: %s\nhasJWT: %s", name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJWT);
    }

    @GET
    @Path("/restricted")
    @RolesAllowed({"admin"})
    public String roleBasedHello(@Context SecurityContext ctx) {
        Principal caller = ctx.getUserPrincipal();
        String name = caller == null ? "anonymous" : caller.getName();
        boolean hasJWT = jwt.getClaimNames() != null;
        return String.format("Hello %s.\nisSecure: %s\nauthScheme: %s\nhasJWT: %s", name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJWT);
    }

    @GET
    @Path("/restricted/subject")
    @RolesAllowed({"admin"})
    public String getSubject() {
        if (jwt.containsClaim(Claims.sub.name())) {
            return jwt.getClaim(Claims.sub.name());
        } else {
            return "No subject claim in JWT";
        }
    }
}
