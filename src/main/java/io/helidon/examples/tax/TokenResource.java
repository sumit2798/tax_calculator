package io.helidon.examples.tax;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Logger;

import jakarta.annotation.security.PermitAll;

@Path("/token")
@RequestScoped
@PermitAll
public class TokenResource {

    private static final Logger LOGGER = Logger.getLogger(TokenResource.class.getName());

    @Inject
    @ConfigProperty(name = "app.jwt.expiration-seconds", defaultValue = "120")
    private long expirationSeconds;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response generateToken(@jakarta.ws.rs.HeaderParam("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .header("WWW-Authenticate", "Basic realm=\"helidon\"")
                    .entity("Unauthorized: Missing or invalid Authorization header")
                    .build();
        }

        try {
            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
            String[] values = credentials.split(":", 2);

            if (values.length != 2 || !"admin".equals(values[0]) || !"password".equals(values[1])) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .header("WWW-Authenticate", "Basic realm=\"helidon\"")
                        .entity("Unauthorized: Invalid credentials")
                        .build();
            }

            String token = createToken(expirationSeconds);
            return Response.ok(token).build();
        } catch (Exception e) {
            LOGGER.severe("Failed to generate token: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to generate token: " + e.getMessage()).build();
        }
    }

    private String createToken(long expirationSeconds) throws Exception {
        // Load private key from classpath
        String keyContent = loadPrivateKeyContent();

        byte[] keyBytes = Base64.getDecoder().decode(keyContent);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(spec);

        // JWT Header
        String header = "{\"alg\":\"RS256\",\"typ\":\"JWT\"}";
        String headerBase64 = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(header.getBytes(StandardCharsets.UTF_8));

        // JWT Payload
        long now = System.currentTimeMillis() / 1000;
        long exp = now + expirationSeconds;
        String payload = "{"
                + "\"iss\": \"https://my-auth-server.com\","
                + "\"sub\": \"admin\","
                + "\"upn\": \"admin\","
                + "\"groups\": [\"admin\"],"
                + "\"iat\": " + now + ","
                + "\"exp\": " + exp + ","
                + "\"jti\": \"unique-id-" + now + "\""
                + "}";
        String payloadBase64 = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));

        // Sign
        String input = headerBase64 + "." + payloadBase64;
        java.security.Signature signature = java.security.Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(input.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = signature.sign();
        String signatureBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);

        return input + "." + signatureBase64;
    }

    private String loadPrivateKeyContent() throws Exception {
        // Try to load from classpath
        try (InputStream is = getClass().getResourceAsStream("/privateKey.pem")) {
            if (is != null) {
                String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                return cleanKeyContent(content);
            }
        }

        // Try to load from environment variable
        String envKey = System.getenv("PRIVATE_KEY_CONTENT");
        if (envKey != null && !envKey.isBlank()) {
            return cleanKeyContent(envKey);
        }

        throw new IllegalStateException(
                "Private key not found. Please ensure 'privateKey.pem' is in classpath or 'PRIVATE_KEY_CONTENT' environment variable is set.");
    }

    private String cleanKeyContent(String content) {
        return content.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
    }
}
