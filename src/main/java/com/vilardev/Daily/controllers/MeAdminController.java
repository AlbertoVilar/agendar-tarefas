package com.vilardev.Daily.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class MeAdminController {

    // Endpoint raiz "/me" acessível apenas por ADMIN
    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> me(@AuthenticationPrincipal UserDetails user) {
        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Map.of(
                "username", user.getUsername(),
                "roles", roles
        );
    }
}