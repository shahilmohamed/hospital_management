package com.project.hospitalReport.controller;

import com.project.hospitalReport.dto.LoginRequest;
import com.project.hospitalReport.dto.LoginResponse;
import com.project.hospitalReport.dto.SignupRequest;
import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.security.JwtUtil;
import com.project.hospitalReport.repository.DoctorRepo;
import com.project.hospitalReport.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private DoctorRepo userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Value("${cookie.domain:}")
    private String cookieDomain;
    
    @Value("${cookie.frontend.domain:}")
    private String frontendCookieDomain;

    @PostMapping("/signup")
    public ApiResponse<String> registerUser(@RequestBody SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ApiResponse<>(null, "User already taken", HttpStatus.BAD_REQUEST.value());
        }
        Doctor user = new Doctor();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());
        user.setDob(request.getDob());
        user.setRole(request.getRole());
        user.setSpecialization(request.getSpecialization());
        userRepository.save(user);
        return new ApiResponse<>(null, "User registered successfully", HttpStatus.OK.value());
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response, HttpServletRequest httpRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<Doctor> doctor = userRepository.findByEmail(userDetails.getUsername());
            if (doctor.isEmpty()) {
                return new ApiResponse<>(null, "User not found", HttpStatus.UNAUTHORIZED.value());
            }
            Doctor user = doctor.get();
            String jwt = jwtUtil.genterateToken((UserDetails) authentication.getPrincipal());

            // 7 days = 7 * 24 * 60 * 60 = 604800 seconds
            long maxAge = 604800L;
            
            // Determine if request is secure (HTTPS) - check both isSecure() and X-Forwarded-Proto header (for proxies)
            boolean isSecure = httpRequest.isSecure() || 
                              "https".equalsIgnoreCase(httpRequest.getHeader("X-Forwarded-Proto"));
            
            // For cross-origin requests, use SameSite=None with Secure=true (HTTPS only)
            // For same-origin or HTTP, use SameSite=Lax with Secure=false
            String sameSite = isSecure ? "None" : "Lax";
            boolean secure = isSecure;
            
            // Extract backend domain from request
            String backendDomain = extractDomainFromRequest(httpRequest);
            
            // Build cookies for backend domain
            ResponseCookie jwtCookieBackend = buildCookie("jwtToken", jwt, maxAge, secure, sameSite, true, backendDomain);
            ResponseCookie idCookieBackend = buildCookie("id", String.valueOf(user.getId()), maxAge, secure, sameSite, false, backendDomain);
            ResponseCookie nameCookieBackend = buildCookie("name", user.getFirstname() + "%20" + user.getLastname(), maxAge, secure, sameSite, false, backendDomain);
            
            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookieBackend.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, idCookieBackend.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, nameCookieBackend.toString());
            
            // Build cookies for frontend domain if configured
            if (frontendCookieDomain != null && !frontendCookieDomain.trim().isEmpty()) {
                ResponseCookie jwtCookieFrontend = buildCookie("jwtToken", jwt, maxAge, secure, sameSite, true, frontendCookieDomain);
                ResponseCookie idCookieFrontend = buildCookie("id", String.valueOf(user.getId()), maxAge, secure, sameSite, false, frontendCookieDomain);
                ResponseCookie nameCookieFrontend = buildCookie("name", user.getFirstname() + "%20" + user.getLastname(), maxAge, secure, sameSite, false, frontendCookieDomain);
                
                response.addHeader(HttpHeaders.SET_COOKIE, jwtCookieFrontend.toString());
                response.addHeader(HttpHeaders.SET_COOKIE, idCookieFrontend.toString());
                response.addHeader(HttpHeaders.SET_COOKIE, nameCookieFrontend.toString());
            }
            
            // Return JWT token in response body as well (for Authorization header approach)
            // This allows frontend to use either cookies or Authorization header
            LoginResponse loginResponse = new LoginResponse(
                jwt,
                user.getId(),
                user.getFirstname() + " " + user.getLastname(),
                "Login successful"
            );
            
            return new ApiResponse<>(loginResponse, "Login successful", HttpStatus.OK.value());
        } catch (BadCredentialsException e) {
            clearCookies(response, httpRequest);
            return new ApiResponse<>(null, "Invalid email or password", HttpStatus.UNAUTHORIZED.value());
        } catch (Exception e) {
            clearCookies(response, httpRequest);
            e.printStackTrace();
            return new ApiResponse<>(null, "Server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    
    /**
     * Helper method to build cookies with consistent configuration
     */
    private ResponseCookie buildCookie(String name, String value, long maxAge, boolean secure, String sameSite, boolean httpOnly, String domain) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .secure(secure)
                .sameSite(sameSite)
                .path("/")
                .maxAge(maxAge);
        
        if (httpOnly) {
            builder.httpOnly(true);
        }
        
        // Set domain if provided
        if (domain != null && !domain.trim().isEmpty()) {
            builder.domain(domain);
        } else if (cookieDomain != null && !cookieDomain.trim().isEmpty()) {
            // Fallback to configured cookie domain if no specific domain provided
            builder.domain(cookieDomain);
        }
        
        return builder.build();
    }
    
    /**
     * Extract domain from HTTP request
     */
    private String extractDomainFromRequest(HttpServletRequest request) {
        String host = request.getHeader("Host");
        if (host == null || host.isEmpty()) {
            return null;
        }
        
        // Remove port if present (e.g., "localhost:8080" -> "localhost")
        int portIndex = host.indexOf(':');
        if (portIndex > 0) {
            host = host.substring(0, portIndex);
        }
        
        // For localhost, return as is
        if ("localhost".equals(host) || "127.0.0.1".equals(host)) {
            return host;
        }
        
        // For production domains, extract the domain
        // If it's a subdomain, you might want to return the parent domain with a dot prefix
        // For example: api.example.com -> .example.com (if you want to share cookies)
        // For now, return the full hostname
        return host;
    }
    
    /**
     * Helper method to clear all authentication cookies
     */
    private void clearCookies(HttpServletResponse response, HttpServletRequest httpRequest) {
        boolean isSecure = httpRequest.isSecure() || 
                          "https".equalsIgnoreCase(httpRequest.getHeader("X-Forwarded-Proto"));
        String sameSite = isSecure ? "None" : "Lax";
        
        // Extract backend domain from request
        String backendDomain = extractDomainFromRequest(httpRequest);
        
        // Clear cookies for backend domain
        ResponseCookie jwtCookieBackend = buildCookie("jwtToken", "", 0, isSecure, sameSite, true, backendDomain);
        ResponseCookie idCookieBackend = buildCookie("id", "", 0, isSecure, sameSite, false, backendDomain);
        ResponseCookie nameCookieBackend = buildCookie("name", "", 0, isSecure, sameSite, false, backendDomain);
        
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookieBackend.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, idCookieBackend.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, nameCookieBackend.toString());
        
        // Clear cookies for frontend domain if configured
        if (frontendCookieDomain != null && !frontendCookieDomain.trim().isEmpty()) {
            ResponseCookie jwtCookieFrontend = buildCookie("jwtToken", "", 0, isSecure, sameSite, true, frontendCookieDomain);
            ResponseCookie idCookieFrontend = buildCookie("id", "", 0, isSecure, sameSite, false, frontendCookieDomain);
            ResponseCookie nameCookieFrontend = buildCookie("name", "", 0, isSecure, sameSite, false, frontendCookieDomain);
            
            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookieFrontend.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, idCookieFrontend.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, nameCookieFrontend.toString());
        }
    }

    @GetMapping("/logout")
    public ApiResponse<?> logout(HttpServletResponse response, HttpServletRequest httpRequest) {
        clearCookies(response, httpRequest);
        return new ApiResponse<>(null, "Logged out successfully", 200);
    }
}