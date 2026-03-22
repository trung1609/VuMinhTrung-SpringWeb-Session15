package com.example.session15.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Map<String, Object> errors = new HashMap<>();
        String exception = (String) request.getAttribute("exception");
        if ("ExpiredJwtException".equals(exception)) {
            errors.put("error", "Token has expired - Please login again");
        } else if ("IllegalArgumentException".equals(exception)) {
            errors.put("error", "Invalid token - Please login again");
        } else if ("UnsupportedJwtException".equals(exception)) {
            errors.put("error", "Unsupported token - Please login again");
        } else if ("MalformedJwtException".equals(exception)) {
            errors.put("error", "Malformed token - Please login again");
        } else {
            errors.put("error", "Unauthorized - Please login again");
        }

        new ObjectMapper().writeValue(response.getOutputStream(),  errors);
    }
}
