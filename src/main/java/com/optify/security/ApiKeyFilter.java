package com.optify.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

// SIN @Component aquí
public class ApiKeyFilter extends OncePerRequestFilter {

    private String apiKey;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/api/") /*&& !path.contains("/users/")*/) {
            String requestKey = request.getHeader("X-API-KEY");
            if (apiKey != null && apiKey.trim().equals(requestKey)) {
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