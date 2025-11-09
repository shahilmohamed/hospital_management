package com.project.hospitalReport.security;

import com.project.hospitalReport.service.DoctorUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Autowired
    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DoctorUserDetailsService doctorServiceV2;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                }
            }
        }

        if (jwtToken != null && jwtUtil.isTokenExpired(jwtToken)){
            Cookie deleteJwtToken = new Cookie("jwtToken", null);
            deleteJwtToken.setHttpOnly(true);
            deleteJwtToken.setSecure(true);
            deleteJwtToken.setPath("/");
            deleteJwtToken.setMaxAge(0);
            response.addCookie(deleteJwtToken);
            Cookie deleteName = new Cookie("name", null);
            deleteName.setHttpOnly(true);
            deleteName.setSecure(true);
            deleteName.setPath("/");
            deleteName.setMaxAge(0);
            response.addCookie(deleteName);
            Cookie deleteId = new Cookie("id", null);
            deleteId.setHttpOnly(true);
            deleteId.setSecure(true);
            deleteId.setPath("/");
            deleteId.setMaxAge(0);
            response.addCookie(deleteId);
            return;
        }

        else if (jwtToken != null && !jwtUtil.isTokenExpired(jwtToken)) {
            try {
                String username = jwtUtil.extractUsername(jwtToken);
                UserDetails userDetails = doctorServiceV2.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                System.out.println("JWT validation failed: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
