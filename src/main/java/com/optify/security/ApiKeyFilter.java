package com.optify.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

// SIN @Component aquí
public class ApiKeyFilter extends OncePerRequestFilter {

    private String apiKey;

    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            "API_KEY_USER",
            null,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_SCRIPT")) // <--- Asignamos el rol aquí
    );

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        boolean isPublicProductEndpoint = path.startsWith("/api/products/categories")
                || path.startsWith("/api/products/category")
                || path.startsWith("/api/products/search")
                || path.startsWith("/api/products/allProducts")
                || path.startsWith("/api/products/mergeProducts");

        if (path.startsWith("/api/") && !isPublicProductEndpoint/*&& !path.contains("/users/")*/) {
            String requestKey = request.getHeader("X-API-KEY");
            if (apiKey != null && apiKey.trim().equals(requestKey)) {
                SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("API Key invalida");
            }
            return;
        }
        filterChain.doFilter(request, response);
    }
}