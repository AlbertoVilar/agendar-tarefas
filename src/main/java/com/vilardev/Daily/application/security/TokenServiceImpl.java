package com.vilardev.Daily.application.security;

import com.vilardev.Daily.infrastructury.entities.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {

    private static final String ISSUER = "daily-tasks-api";

    private final SecretKey secretKey;
    private final long expirationMinutes;

    public TokenServiceImpl(@Value("${api.security.token.secret}") String secret,
                            @Value("${api.security.token.expiration-minutes}") long expirationMinutes) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    @Override
    public String generateToken(Usuario usuario) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiry = Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES));

        String roles = usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(usuario.getUsername())
                .claim("roles", roles)
                .setIssuedAt(issuedAt)
                .setExpiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            return parseClaims(token) != null;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        if (token == null) return null;
        try {
            var claims = parseClaims(token);
            return claims == null ? null : claims.getSubject();
        } catch (Exception ex) {
            return null;
        }
    }

    // Reflection-based parser to avoid compile-time dependency on parserBuilder API
    private io.jsonwebtoken.Claims parseClaims(String token) {
        try {
            Class<?> jwtsClass = Class.forName("io.jsonwebtoken.Jwts");

            // Try parserBuilder() first
            try {
                java.lang.reflect.Method parserBuilderMethod = jwtsClass.getMethod("parserBuilder");
                Object builder = parserBuilderMethod.invoke(null);

                // try setSigningKey(Key) or setSigningKey(byte[])
                try {
                    java.lang.reflect.Method setKey = builder.getClass().getMethod("setSigningKey", java.security.Key.class);
                    setKey.invoke(builder, secretKey);
                } catch (NoSuchMethodException e) {
                    java.lang.reflect.Method setKey = builder.getClass().getMethod("setSigningKey", byte[].class);
                    setKey.invoke(builder, (Object) secretKey.getEncoded());
                }

                Object parser = builder.getClass().getMethod("build").invoke(builder);
                Object jws = parser.getClass().getMethod("parseClaimsJws", String.class).invoke(parser, token);
                Object body = jws.getClass().getMethod("getBody").invoke(jws);
                return (io.jsonwebtoken.Claims) body;
            } catch (NoSuchMethodException ns) {
                // Fallback to older parser() API
                java.lang.reflect.Method parserMethod = jwtsClass.getMethod("parser");
                Object parser = parserMethod.invoke(null);

                // try setSigningKey(Key) or setSigningKey(byte[])
                try {
                    java.lang.reflect.Method setKey = parser.getClass().getMethod("setSigningKey", java.security.Key.class);
                    setKey.invoke(parser, secretKey);
                } catch (NoSuchMethodException e) {
                    java.lang.reflect.Method setKey = parser.getClass().getMethod("setSigningKey", byte[].class);
                    setKey.invoke(parser, (Object) secretKey.getEncoded());
                }

                Object jws = parser.getClass().getMethod("parseClaimsJws", String.class).invoke(parser, token);
                Object body = jws.getClass().getMethod("getBody").invoke(jws);
                return (io.jsonwebtoken.Claims) body;
            }
        } catch (Exception ex) {
            return null;
        }
    }
}
