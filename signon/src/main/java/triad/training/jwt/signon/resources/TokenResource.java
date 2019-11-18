package triad.training.jwt.signon.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import triad.training.jwt.signon.services.TokenService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.Callable;

@Path("/token")
@Produces(MediaType.TEXT_PLAIN)
public class TokenResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenResource.class);
    private final TokenService tokenService;

    @Inject
    public TokenResource(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GET
    @Path("{user}/{password}")
    public Response getToken(@PathParam("user") String user, @PathParam("password") String password) {
        LOGGER.info("GET JWT for {}", user);
        return serviceCall(() -> tokenService.getToken(user, password));
    }

    @GET
    @Path("/public-key")
    public Response getPublicKey() {
        LOGGER.info("GET Public Key");
        return Response.ok(tokenService.getPublicKey()).build();
    }

    private <T> Response serviceCall(Callable<Optional<T>> callable) {
        try {
            Optional<T> optional = callable.call();
            if (optional.isPresent()) {
                return Response.ok(optional.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
