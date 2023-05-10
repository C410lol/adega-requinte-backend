package com.adega.ms.user.adegauserservice.configs.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        var authPrincipal = request.getHeader("Authentication-Principal");
        var authCredentials = request.getHeader("Authentication-Credentials");
        var authAuthorities = request.getHeader("Authentication-Authorities");
        if(authPrincipal != null && authCredentials != null && authAuthorities != null) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    request.getHeader(authPrincipal),
                    request.getHeader(authCredentials),
                    List.of(new SimpleGrantedAuthority(authAuthorities))
            ));
        }
        filterChain.doFilter(request, response);
    }

}
