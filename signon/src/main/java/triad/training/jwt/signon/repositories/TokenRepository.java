package triad.training.jwt.signon.repositories;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class TokenRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenRepository.class);
    private Map<String, String> userToJwtClaims = new HashMap<>();

    public TokenRepository() {
        String userHash = new DigestUtils("SHA-256").digestAsHex("dwagstaff");
        String passwordHash = new DigestUtils("SHA-256").digestAsHex("myPa55w0rd!");
        String userPasswordHash = new DigestUtils("SHA-256").digestAsHex(userHash + passwordHash);
        LOGGER.info("Creating JWT with user hash {}, password hash {} and user-password hash {}", userHash, passwordHash, userPasswordHash);
        String jwt = "{\n" +
                "  \"iss\": \"https://triad.io/jwt-test\",\n" +
                "  \"sub\": \"dwagstaff\",\n" +
                "  \"roleMappings\": {\n" +
                "    \"group1\": \"Group1MappedRole\",\n" +
                "    \"group2\": \"Group2MappedRole\"\n" +
                "  },\n" +
                "  \"groups\": [\n" +
                "    \"admin\"\n" +
                "  ]\n" +
                "}";
        userToJwtClaims.put(userPasswordHash, jwt);
    }

    public Optional<String> get(String userPasswordHash) {
        return Optional.ofNullable(userToJwtClaims.get(userPasswordHash));
    }
}
