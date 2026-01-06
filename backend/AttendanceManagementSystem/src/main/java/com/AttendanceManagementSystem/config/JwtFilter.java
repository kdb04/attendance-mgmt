package com.AttendanceManagementSystem.config;

import com.AttendanceManagementSystem.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(
        ServletRequest req,
        ServletResponse res,
        FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        //System.out.println("JwtFilter path = " + path);

        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || path.startsWith("/api/auth")) {
            chain.doFilter(req, res);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        //System.out.println("Authorization header = " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(response, 401, "Missing or invalid Authorization header");
            return;
        }

        try {
            String token = authHeader.substring(7);
            Claims claims = jwtUtil.validateToken(token);

            String role = claims.get("role", String.class);
            String userId = claims.getSubject();

            //Admin full access
            if("ADMIN".equals(role)) {
                chain.doFilter(req, res);
                return;
            }

            //Student-only access
            if (path.startsWith("/api/students")){
                if (!"STUDENT".equals(role) || !path.contains("/" + userId + "/")) {
                    sendError(response, 403, "Students can only access their own data");
                    return;
                }
            }

            //Teacher-only access
            if (path.startsWith("/api/teachers")){
                if (!"TEACHER".equals(role) || !path.contains("/" + userId + "/")) {
                    sendError(response, 403, "Teachers can only access their own data");
                    return;
                }
            }

            //Admin-only routes
            if (path.startsWith("/api/admin")) {
                sendError(response, 403, "Admin access required");
                return;
            }          

            request.setAttribute("userId", userId);
            request.setAttribute("role", role);

            //System.out.println("JWT FILTER HIT");
            //System.out.println("Path: " + path);
            //System.out.println("User: " + claims.getSubject());
            //System.out.println("Role: " + claims.get("role"));

        } catch (Exception e) {
            sendError(response, 401, "Invalid or expired JWT token");
            return;
        }

        chain.doFilter(req, res);
    }

    private void sendError(
        HttpServletResponse response,
        int status,
        String message
    ) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("""
            {
                "error": "%s",
                "status": %d
            }
        """.formatted(message, status));
    }
}
