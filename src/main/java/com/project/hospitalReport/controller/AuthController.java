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
            
            // Determine if request is secure (HTTPS) - check multiple headers for proxies/load balancers
            boolean isSecure = httpRequest.isSecure() || 
                              "https".equalsIgnoreCase(httpRequest.getHeader("X-Forwarded-Proto")) ||
                              "https".equalsIgnoreCase(httpRequest.getHeader("X-Forwarded-Scheme")) ||
                              "on".equalsIgnoreCase(httpRequest.getHeader("X-Forwarded-Ssl"));
            
            // Get origin for CORS and cookie handling
            String origin = httpRequest.getHeader("Origin");
            if (origin == null || origin.isEmpty()) {
                // Fallback: construct origin from request
                String scheme = isSecure ? "https" : "http";
                String host = httpRequest.getHeader("Host");
                if (host != null && !host.isEmpty()) {
                    origin = scheme + "://" + host;
                }
            }
            
            // Detect mobile browser for iOS/Android specific handling
            String userAgent = httpRequest.getHeader("User-Agent");
            boolean isMobileBrowser = isMobileDevice(userAgent);
            
            // Set CORS headers explicitly for mobile browsers
            if (origin != null && !origin.isEmpty()) {
                response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");
                // Additional headers for mobile compatibility
                response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
                response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");
            }
            
            // For mobile browsers (iOS Safari, Android Chrome): SameSite=None REQUIRES Secure=true
            // iOS Safari and Android Chrome are very strict about cookie settings
            String sameSite;
            boolean secure;
            if (isSecure) {
                // HTTPS: Use SameSite=None for cross-origin cookies (required for iOS/Android)
                sameSite = "None";
                secure = true;
            } else {
                // HTTP: Use Lax (won't work for cross-origin on mobile, but will work same-origin)
                // Note: For mobile browsers, HTTP with cross-origin cookies will NOT work
                // You MUST use HTTPS for cross-origin cookies on iOS/Android
                sameSite = "Lax";
                secure = false;
                
                // Log warning for mobile browsers on HTTP
                if (isMobileBrowser) {
                    System.out.println("WARNING: Mobile browser detected on HTTP. Cookies may not work cross-origin. HTTPS required for mobile cookie support.");
                }
            }
            
            // Extract backend domain from request (handle mobile scenarios)
            String backendDomain = extractDomainFromRequest(httpRequest);
            
            // For mobile: Don't set domain if it's an IP address or localhost
            // Setting domain on IP/localhost can cause cookies to not be set
            String cookieDomainToUse = null;
            if (backendDomain != null && !backendDomain.isEmpty()) {
                // Only set domain if it's a proper domain name (not IP or localhost)
                if (!backendDomain.equals("localhost") && 
                    !backendDomain.equals("127.0.0.1") && 
                    !backendDomain.matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$") &&
                    !backendDomain.startsWith("192.168.") &&
                    !backendDomain.startsWith("10.") &&
                    !backendDomain.startsWith("172.")) {
                    cookieDomainToUse = backendDomain;
                }
            }
            
            // Build cookies for backend domain
            ResponseCookie jwtCookieBackend = buildCookie("jwtToken", jwt, maxAge, secure, sameSite, true, cookieDomainToUse);
            ResponseCookie idCookieBackend = buildCookie("id", String.valueOf(user.getId()), maxAge, secure, sameSite, false, cookieDomainToUse);
            ResponseCookie nameCookieBackend = buildCookie("name", user.getFirstname() + "%20" + user.getLastname(), maxAge, secure, sameSite, false, cookieDomainToUse);
            
            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookieBackend.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, idCookieBackend.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, nameCookieBackend.toString());
            
            // Build cookies for frontend domain if configured (only if it's a proper domain)
            if (frontendCookieDomain != null && !frontendCookieDomain.trim().isEmpty()) {
                // Only set frontend domain cookies if it's not an IP address
                if (!frontendCookieDomain.matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$") &&
                    !frontendCookieDomain.startsWith("192.168.") &&
                    !frontendCookieDomain.startsWith("10.") &&
                    !frontendCookieDomain.startsWith("172.")) {
                    ResponseCookie jwtCookieFrontend = buildCookie("jwtToken", jwt, maxAge, secure, sameSite, true, frontendCookieDomain);
                    ResponseCookie idCookieFrontend = buildCookie("id", String.valueOf(user.getId()), maxAge, secure, sameSite, false, frontendCookieDomain);
                    ResponseCookie nameCookieFrontend = buildCookie("name", user.getFirstname() + "%20" + user.getLastname(), maxAge, secure, sameSite, false, frontendCookieDomain);
                    
                    response.addHeader(HttpHeaders.SET_COOKIE, jwtCookieFrontend.toString());
                    response.addHeader(HttpHeaders.SET_COOKIE, idCookieFrontend.toString());
                    response.addHeader(HttpHeaders.SET_COOKIE, nameCookieFrontend.toString());
                }
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
     * Important for mobile: SameSite=None requires Secure=true
     */
    private ResponseCookie buildCookie(String name, String value, long maxAge, boolean secure, String sameSite, boolean httpOnly, String domain) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .path("/")
                .maxAge(maxAge);
        
        // CRITICAL: SameSite=None REQUIRES Secure=true (especially for mobile browsers)
        if ("None".equals(sameSite)) {
            builder.secure(true); // Force secure when SameSite=None
            builder.sameSite("None");
        } else {
            builder.secure(secure);
            builder.sameSite(sameSite);
        }
        
        if (httpOnly) {
            builder.httpOnly(true);
        }
        
        // Set domain if provided (but not for IP addresses or localhost)
        if (domain != null && !domain.trim().isEmpty()) {
            // Don't set domain for IP addresses or localhost (mobile browsers reject these)
            if (!domain.equals("localhost") && 
                !domain.equals("127.0.0.1") && 
                !domain.matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$") &&
                !domain.startsWith("192.168.") &&
                !domain.startsWith("10.") &&
                !domain.startsWith("172.")) {
                builder.domain(domain);
            }
        } else if (cookieDomain != null && !cookieDomain.trim().isEmpty()) {
            // Fallback to configured cookie domain if no specific domain provided
            // But only if it's a proper domain name
            if (!cookieDomain.equals("localhost") && 
                !cookieDomain.equals("127.0.0.1") && 
                !cookieDomain.matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$") &&
                !cookieDomain.startsWith("192.168.") &&
                !cookieDomain.startsWith("10.") &&
                !cookieDomain.startsWith("172.")) {
                builder.domain(cookieDomain);
            }
        }
        
        return builder.build();
    }
    
    /**
     * Detect if the request is from a mobile device (iOS or Android)
     */
    private boolean isMobileDevice(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return false;
        }
        String ua = userAgent.toLowerCase();
        // Check for iOS (Safari, Chrome on iOS, etc.)
        boolean isIOS = ua.contains("iphone") || ua.contains("ipad") || ua.contains("ipod") || 
                       (ua.contains("macintosh") && ua.contains("mobile"));
        // Check for Android
        boolean isAndroid = ua.contains("android");
        // Check for mobile browsers in general
        boolean isMobile = ua.contains("mobile") || ua.contains("tablet");
        return isIOS || isAndroid || isMobile;
    }
    
    /**
     * Extract domain from HTTP request
     * Handles mobile requests, proxies, and load balancers
     */
    private String extractDomainFromRequest(HttpServletRequest request) {
        // Try multiple headers for domain extraction (important for mobile/proxy scenarios)
        String host = request.getHeader("Host");
        if (host == null || host.isEmpty()) {
            // Fallback to X-Forwarded-Host (common in proxy/load balancer setups)
            host = request.getHeader("X-Forwarded-Host");
        }
        if (host == null || host.isEmpty()) {
            // Fallback to server name
            host = request.getServerName();
        }
        if (host == null || host.isEmpty()) {
            return null;
        }
        
        // Remove port if present (e.g., "localhost:8080" -> "localhost")
        int portIndex = host.indexOf(':');
        if (portIndex > 0) {
            host = host.substring(0, portIndex);
        }
        
        // For localhost or IP addresses, return as is (but don't set as cookie domain)
        if ("localhost".equals(host) || "127.0.0.1".equals(host) || host.matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$")) {
            return host;
        }
        
        // For production domains, return the hostname
        // Note: For subdomains, you might want to return parent domain with dot prefix
        // Example: api.example.com -> .example.com (to share cookies across subdomains)
        return host;
    }
    
    /**
     * Helper method to clear all authentication cookies
     * Uses same mobile-friendly logic as login
     */
    private void clearCookies(HttpServletResponse response, HttpServletRequest httpRequest) {
        // Determine if request is secure (check multiple headers for proxies)
        boolean isSecure = httpRequest.isSecure() || 
                          "https".equalsIgnoreCase(httpRequest.getHeader("X-Forwarded-Proto")) ||
                          "https".equalsIgnoreCase(httpRequest.getHeader("X-Forwarded-Scheme")) ||
                          "on".equalsIgnoreCase(httpRequest.getHeader("X-Forwarded-Ssl"));
        
        // SameSite=None requires Secure=true for mobile browsers
        String sameSite = isSecure ? "None" : "Lax";
        boolean secure = isSecure;
        
        // Extract backend domain from request
        String backendDomain = extractDomainFromRequest(httpRequest);
        
        // Don't set domain for IP addresses or localhost (mobile browsers reject these)
        String cookieDomainToUse = null;
        if (backendDomain != null && !backendDomain.isEmpty()) {
            if (!backendDomain.equals("localhost") && 
                !backendDomain.equals("127.0.0.1") && 
                !backendDomain.matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$") &&
                !backendDomain.startsWith("192.168.") &&
                !backendDomain.startsWith("10.") &&
                !backendDomain.startsWith("172.")) {
                cookieDomainToUse = backendDomain;
            }
        }
        
        // Clear cookies for backend domain
        ResponseCookie jwtCookieBackend = buildCookie("jwtToken", "", 0, secure, sameSite, true, cookieDomainToUse);
        ResponseCookie idCookieBackend = buildCookie("id", "", 0, secure, sameSite, false, cookieDomainToUse);
        ResponseCookie nameCookieBackend = buildCookie("name", "", 0, secure, sameSite, false, cookieDomainToUse);
        
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookieBackend.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, idCookieBackend.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, nameCookieBackend.toString());
        
        // Clear cookies for frontend domain if configured (only if it's a proper domain)
        if (frontendCookieDomain != null && !frontendCookieDomain.trim().isEmpty()) {
            if (!frontendCookieDomain.matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$") &&
                !frontendCookieDomain.startsWith("192.168.") &&
                !frontendCookieDomain.startsWith("10.") &&
                !frontendCookieDomain.startsWith("172.")) {
                ResponseCookie jwtCookieFrontend = buildCookie("jwtToken", "", 0, secure, sameSite, true, frontendCookieDomain);
                ResponseCookie idCookieFrontend = buildCookie("id", "", 0, secure, sameSite, false, frontendCookieDomain);
                ResponseCookie nameCookieFrontend = buildCookie("name", "", 0, secure, sameSite, false, frontendCookieDomain);
                
                response.addHeader(HttpHeaders.SET_COOKIE, jwtCookieFrontend.toString());
                response.addHeader(HttpHeaders.SET_COOKIE, idCookieFrontend.toString());
                response.addHeader(HttpHeaders.SET_COOKIE, nameCookieFrontend.toString());
            }
        }
    }

    @GetMapping("/logout")
    public ApiResponse<?> logout(HttpServletResponse response, HttpServletRequest httpRequest) {
        clearCookies(response, httpRequest);
        return new ApiResponse<>(null, "Logged out successfully", 200);
    }
}