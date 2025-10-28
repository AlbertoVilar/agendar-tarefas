package com.vilardev.Daily.infrastructury.security;

import com.vilardev.Daily.application.security.TokenService;
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

    public JwtAuthenticationFilter(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        System.out.println("JwtAuthenticationFilter - Authorization header: " + header);

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("JwtAuthenticationFilter - Extracted token: " + token.substring(0, Math.min(20, token.length())) + "...");

            try {
                boolean isValid = tokenService.validateToken(token);
                System.out.println("JwtAuthenticationFilter - Token valid: " + isValid);
                
                if (isValid) {
                    String username = tokenService.getUsernameFromToken(token);
                    System.out.println("JwtAuthenticationFilter - Username from token: " + username);
                    
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        System.out.println("JwtAuthenticationFilter - UserDetails loaded: " + userDetails.getUsername());
                        
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("JwtAuthenticationFilter - Authentication set in SecurityContext");
                    }
                }
            } catch (Exception ex) {
                System.out.println("JwtAuthenticationFilter - Exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            System.out.println("JwtAuthenticationFilter - No Bearer token found");
        }

        filterChain.doFilter(request, response);
    }
}

