package ru.marthastudios.robloxcasino.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtAccessTokenUtil jwtAccessTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = null;

        if (request.getHeader("Authorization") != null && request.getHeader("Authorization").startsWith("Bearer ")){
            authToken = request.getHeader("Authorization").substring(7);
        }

        if (authToken != null && jwtAccessTokenUtil.validateToken(authToken)){
            Authentication authentication = new UsernamePasswordAuthenticationToken(jwtAccessTokenUtil.extractSubjectFromToken(authToken), null);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
