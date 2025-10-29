package com.vilardev.Daily.application.security;

import com.vilardev.Daily.infrastructury.entities.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);

    private final SecretKey secretKey;
    private final long expirationMinutes;

    public TokenServiceImpl(@Value("${api.security.token.secret}") String secret,
                            @Value("${api.security.token.expiration-minutes}") long expirationMinutes) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
        if (log.isDebugEnabled()) {
            log.debug("TokenServiceImpl initialized. ExpirationMinutes={} keyLength={} bytes", expirationMinutes, secretKey.getEncoded().length);
        }
    }

    @Override
    public String generateToken(Usuario usuario) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiry = Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES));

        String roles = usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        if (log.isDebugEnabled()) {
            log.debug("Generating token. subject={} roles={} expInMinutes={}", usuario.getUsername(), roles, expirationMinutes);
        }

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
            boolean ok = parseClaims(token) != null;
            if (log.isDebugEnabled()) {
                log.debug("validateToken -> {}", ok);
            }
            return ok;
        } catch (Exception ex) {
            log.error("validateToken exception: {} - {}", ex.getClass().getName(), ex.getMessage(), ex);
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
            log.error("getUsernameFromToken exception: {} - {}", ex.getClass().getName(), ex.getMessage(), ex);
            return null;
        }
    }

    // JJWT 0.12.x parser implementation
    private io.jsonwebtoken.Claims parseClaims(String token) {
        try {
            var jwt = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            if (log.isDebugEnabled()) {
                log.debug("parseClaims OK. subject={} exp={} issuer={}", jwt.getPayload().getSubject(), jwt.getPayload().getExpiration(), jwt.getPayload().getIssuer());
            }
            return jwt.getPayload();
        } catch (Exception ex) {
            log.error("parseClaims exception: {} - {}", ex.getClass().getName(), ex.getMessage(), ex);
            return null;
        }
    }
}
