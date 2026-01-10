package com.AttendanceManagementSystem.config;

import com.AttendanceManagementSystem.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final String frontendOrigin;

    public JwtFilter(JwtUtil jwtUtil, @Value("${FRONTEND_ORIGIN}") String frontendOrigin) {
        this.jwtUtil = jwtUtil;
        this.frontendOrigin = frontendOrigin;
    }

    @Override
    public void doFilter(
        ServletRequest req,
        ServletResponse res,
        FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", frontendOrigin);
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "3600");

        String path = request.getRequestURI();
        String method = request.getMethod();

        //System.out.println("JwtFilter path = " + path);

        if ("OPTIONS".equalsIgnoreCase(method) || path.startsWith("/api/auth") || path.startsWith("/api/test")) {
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

            //Admin has access to everything
            if("ADMIN".equals(role)) {
                chain.doFilter(req, res);
                return;
            }

            //Student-only access
            if (path.startsWith("/api/students")){
                if (!"STUDENT".equals(role)) {
                    sendError(response, 403, "Students access required");
                    return;
                }

                String base = "/api/students/";
                String remainingPath = path.substring(base.length());
                String requestedId = remainingPath.split("/")[0];

                if(!requestedId.equals(userId)) {
                    sendError(response, 403, "Students can only access their own data");
                    return;
                }
            }

            //Teacher-only access
            if (path.startsWith("/api/teachers")){
                if (!"TEACHER".equals(role)) {
                    sendError(response, 403, "Teachers access required");
                    return;
                }

                String base = "/api/teachers/";
                String remainingPath = path.substring(base.length());
                String requestedId = remainingPath.split("/")[0];

                if(!requestedId.equals(userId)) {
                    sendError(response, 403, "Teachers can only access their own data");
                    return;
                }
            }

            //Courses - all have access
            if (path.startsWith("/api/courses")){
                //Anyone logged in can access courses
                if("GET".equalsIgnoreCase(method)){
                    chain.doFilter(req, res);
                    return;
                }
                //Only Admin can modify courses
                if(!"ADMIN".equals(role)){
                    sendError(response, 403, "Admin access required to modify courses");
                    return;
                }
            }

            //Sessions - teacher and admin access
            if (path.startsWith("/api/sessions/course")){
                if(!"ADMIN".equals(role) && !"TEACHER".equals(role)) {
                    sendError(response, 403, "Teacher or Admin access required");
                    return;
                }

                //api/sessions/course/{coursecode}/teacher/{trn}
                String parts[] = path.split("/");
                String trn = parts[parts.length - 1];

                if(!trn.equalsIgnoreCase(userId)){
                    sendError(response, 403, "Teachers can only access their own sessions");
                    return;
                }
            }

            //Admin-only access
            if (path.startsWith("/api/admin")) {
                if (!"ADMIN".equals(role)) {
                    sendError(response, 403, "Admin access required");
                    return;
                }
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

        response.setHeader("Access-Control-Allow-Origin", frontendOrigin);
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization,Content-Type");

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
