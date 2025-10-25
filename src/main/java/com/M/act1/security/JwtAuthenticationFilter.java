package com.M.act1.security;

import com.M.act1.service.UserService;
import com.M.act1.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenStore tokenStore; // ✅ inject the TokenStore

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // ✅ check if token was already logged out
            if (!tokenStore.isValid(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or logged-out token");
                return;
            }

            String username = jwtUtil.extractUsername(token);
            if (username != null) {
                var user = userService.findByUsername(username);
                // You can set the security context here if needed
            }
        }

        filterChain.doFilter(request, response);
    }
}
