package by.innowise.user_service.util;

import by.innowise.user_service.exception.JwtAuthenticationException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JwtUtil {
    @Value("${JWT_ACCESS_PUBLIC_KEY}")
    private String publicKeyBase64;

    private PublicKey publicKey;

    @PostConstruct
    public void init() throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = Base64.getDecoder().decode(publicKeyBase64);

        publicKey = KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(bytes));
    }

    public String getLoginFromToken(String token) throws JwtAuthenticationException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("login", String.class);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Invalid access token");
        }
    }
}
