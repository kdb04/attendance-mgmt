package com.AttendanceManagementSystem.config;

import com.AttendanceManagementSystem.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JwtFilter implements Filter {

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

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(req, res);
            return;
        }   

        // Allow auth endpoint
        if (path.startsWith("/api/auth")) {
            chain.doFilter(req, res);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        //System.out.println("Authorization header = " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            String token = authHeader.substring(7);
            Claims claims = JwtUtil.validateToken(token);

            //Admin-only protection 
            if (path.startsWith("/api/admin") && !"ADMIN".equals(claims.get("role"))) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Unauthorized - JWT missing or invalid");
                return;
            }

            request.setAttribute("userId", claims.getSubject());
            request.setAttribute("role", claims.get("role"));

            //System.out.println("JWT FILTER HIT");
            //System.out.println("Path: " + path);
            //System.out.println("User: " + claims.getSubject());
            //System.out.println("Role: " + claims.get("role"));

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(req, res);
    }
}
