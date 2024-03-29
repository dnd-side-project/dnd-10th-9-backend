package com.dnd.dotchi.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> ALLOWED_URIS = List.of(
            "/swagger-ui.html",
            "/favicon.ico",
            "/health-check"
    );

    private static final List<String> ALLOWED_START_URIS = List.of(
            "/v3/api-docs",
            "/swagger-ui",
            "/h2-console"
    );

    private static final List<String> ALLOWED_END_URIS = List.of(
            "/login"
    );

    private final TokenProcessor tokenProcessor;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String tokenWithoutType = tokenProcessor.resolveToken(token);
        tokenProcessor.validateToken(tokenWithoutType);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        return containsAllowedUris(request) || startsWithAllowedStartUris(request)
                || matchesGuestRequest(request);
    }

    private boolean containsAllowedUris(final HttpServletRequest request) {
        return ALLOWED_URIS.stream()
                .anyMatch(url -> request.getRequestURI().contains(url));
    }

    private boolean startsWithAllowedStartUris(final HttpServletRequest request) {
        return ALLOWED_START_URIS.stream()
                .anyMatch(url -> request.getRequestURI().startsWith(url));
    }

    private boolean matchesGuestRequest(final HttpServletRequest request) {
        return ALLOWED_END_URIS.stream()
                .anyMatch(url -> request.getRequestURI().endsWith(url));
    }

}
