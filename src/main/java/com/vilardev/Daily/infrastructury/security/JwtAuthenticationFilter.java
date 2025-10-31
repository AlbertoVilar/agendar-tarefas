package com.vilardev.Daily.infrastructury.security;

import com.vilardev.Daily.application.security.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro que extrai o JWT do header Authorization, valida via TokenService,
 * carrega o UserDetails e popula o SecurityContext.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (log.isDebugEnabled()) {
            log.debug("JwtAuthenticationFilter - Authorization header present? {}", header != null);
        }

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (log.isDebugEnabled()) {
                String preview = token.substring(0, Math.min(12, token.length()));
                log.debug("JwtAuthenticationFilter - Extracted token preview: {}...", preview);
            }

            try {
                boolean isValid = tokenService.validateToken(token);
                if (log.isDebugEnabled()) {
                    log.debug("JwtAuthenticationFilter - Token valid: {}", isValid);
                }
                
                if (isValid) {
                    String username = tokenService.getUsernameFromToken(token);
                    if (log.isDebugEnabled()) {
                        log.debug("JwtAuthenticationFilter - Username from token: {}", username);
                    }
                    
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        if (log.isDebugEnabled()) {
                            log.debug("JwtAuthenticationFilter - UserDetails loaded: {}", userDetails.getUsername());
                        }
                        
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        if (log.isDebugEnabled()) {
                            log.debug("JwtAuthenticationFilter - Authentication set in SecurityContext");
                        }
                    }
                }
            } catch (Exception ex) {
                log.error("JwtAuthenticationFilter - Exception during token processing: {}", ex.getMessage(), ex);
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("JwtAuthenticationFilter - No Bearer token found");
            }
        }

        filterChain.doFilter(request, response);
    }
}

