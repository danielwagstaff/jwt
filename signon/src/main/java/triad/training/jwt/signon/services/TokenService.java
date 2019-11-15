package triad.training.jwt.signon.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import triad.training.jwt.signon.repositories.TokenRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;

@ApplicationScoped
public class TokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    @ConfigProperty(name = "mp.jwt.verify.publickey")
    String publicKey;

    @ConfigProperty(name = "jwt.privatekey.location")
    String privateKeyLocation;

    private final TokenRepository tokenRepository;

    @Inject
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Optional<String> getToken(final String user, final String passwordHash) throws Exception {
        String userHash = new DigestUtils("SHA-256").digestAsHex(user);
        String userPasswordHash = new DigestUtils("SHA-256").digestAsHex(userHash + passwordHash);
        LOGGER.info("userHash {} passwordHash {} userPasswordHash {}", userHash, passwordHash, userPasswordHash);
        LOGGER.info("Retrieving JWT for {} with key {}", user, userPasswordHash);
        Optional<String> optJwt = tokenRepository.get(userPasswordHash);
        if (optJwt.isPresent()) {
            return Optional.of(generateTokenString(optJwt.get()));
        } else {
            return Optional.empty();
        }
    }

    public String getPublicKey() {
        LOGGER.info("Retrieving Public Key");
        return publicKey;
    }

    private String generateTokenString(String jwt) throws Exception {
        LOGGER.info("Generating Token from JWT");

        JwtClaims claims = JwtClaims.parse(jwt);
        claims.setIssuedAt(NumericDate.fromSeconds(currentTimeInSecs()));
        claims.setExpirationTime(NumericDate.fromSeconds(currentTimeInSecs() + 300L));

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(readPrivateKey());
        jws.setHeader("typ", "JWT");
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        return jws.getCompactSerialization();
    }

    private PrivateKey readPrivateKey() throws Exception {
        InputStream contentIS = TokenService.class.getResourceAsStream(privateKeyLocation);
        byte[] tmp = new byte[4096];
        int length = contentIS.read(tmp);
        return decodePrivateKey(new String(tmp, 0, length, StandardCharsets.UTF_8));
    }

    private PrivateKey decodePrivateKey(final String pemEncoded) throws Exception {
        byte[] encodedBytes = toEncodedBytes(pemEncoded);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    private byte[] toEncodedBytes(final String pemEncoded) {
        final String normalizedPem = removeBeginEnd(pemEncoded);
        return Base64.getDecoder().decode(normalizedPem);
    }

    private String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        return pem.trim();
    }

    private int currentTimeInSecs() {
        long currentTimeMS = System.currentTimeMillis();
        return (int) (currentTimeMS / 1000);
    }
}
