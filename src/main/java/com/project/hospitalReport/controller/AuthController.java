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

            long maxAge = 604800L;

            boolean isSecure = httpRequest.isSecure() || 
                              "https".equalsIgnoreCase(httpRequest.getHeader("X-Forwarded-Proto"));

            String sameSite = isSecure ? "None" : "Lax";
            boolean secure = isSecure;

            String backendDomain = extractDomainFromRequest(httpRequest);

            ResponseCookie jwtCookieBackend = buildCookie("jwtToken", jwt, maxAge, secure, sameSite, true, backendDomain);
            ResponseCookie idCookieBackend = buildCookie("id", String.valueOf(user.getId()), maxAge, secure, sameSite, false, backendDomain);
            ResponseCookie nameCookieBackend = buildCookie("name", user.getFirstname() + "%20" + user.getLastname(), maxAge, secure, sameSite, false, backendDomain);
            
            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookieBackend.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, idCookieBackend.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, nameCookieBackend.toString());

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

    private ResponseCookie buildCookie(String name, String value, long maxAge, boolean secure, String sameSite, boolean httpOnly, String domain) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(name, value)
                .secure(secure)
                .sameSite(sameSite)
                .path("/")
                .maxAge(maxAge);
        
        if (httpOnly) {
            builder.httpOnly(true);
        }

        if (domain != null && !domain.trim().isEmpty()) {
            builder.domain(domain);
        } else if (cookieDomain != null && !cookieDomain.trim().isEmpty()) {
            builder.domain(cookieDomain);
        }
        
        return builder.build();
    }

    private String extractDomainFromRequest(HttpServletRequest request) {
        String host = request.getHeader("Host");
        if (host == null || host.isEmpty()) {
            return null;
        }
        int portIndex = host.indexOf(':');
        if (portIndex > 0) {
            host = host.substring(0, portIndex);
        }
        if ("localhost".equals(host) || "127.0.0.1".equals(host)) {
            return host;
        }
        return host;
    }

    private void clearCookies(HttpServletResponse response, HttpServletRequest httpRequest) {
        boolean isSecure = httpRequest.isSecure() || 
                          "https".equalsIgnoreCase(httpRequest.getHeader("X-Forwarded-Proto"));
        String sameSite = isSecure ? "None" : "Lax";

        String backendDomain = extractDomainFromRequest(httpRequest);
        ResponseCookie jwtCookieBackend = buildCookie("jwtToken", "", 0, isSecure, sameSite, true, backendDomain);
        ResponseCookie idCookieBackend = buildCookie("id", "", 0, isSecure, sameSite, false, backendDomain);
        ResponseCookie nameCookieBackend = buildCookie("name", "", 0, isSecure, sameSite, false, backendDomain);
        
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookieBackend.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, idCookieBackend.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, nameCookieBackend.toString());

    }

    @GetMapping("/logout")
    public ApiResponse<?> logout(HttpServletResponse response, HttpServletRequest httpRequest) {
        clearCookies(response, httpRequest);
        return new ApiResponse<>(null, "Logged out successfully", 200);
    }
}