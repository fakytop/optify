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
        boolean isPublicProductEndpoint = path.startsWith("/api/products/categories")
                || path.startsWith("/api/products/category")
                || path.startsWith("/api/products/search")
                || path.startsWith("/api/products/allProducts")
                || path.startsWith("/api/products/mergeProducts")
                || path.startsWith("/api/products/changeProductReference")
                || path.startsWith("/api/products/getStoreProductsByProduct")
                || path.startsWith("/api/products/manualImport")
                || path.startsWith("/api/products/deleteProductReference")
                || path.startsWith("/api/products/manualImportToProduct");

        if (path.startsWith("/api/") && !isPublicProductEndpoint/*&& !path.contains("/users/")*/) {
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