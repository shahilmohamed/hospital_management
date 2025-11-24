package com.project.hospitalReport.controller;

import com.project.hospitalReport.dto.LoginRequest;
import com.project.hospitalReport.dto.SignupRequest;
import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.security.JwtUtil;
import com.project.hospitalReport.repository.DoctorRepo;
import com.project.hospitalReport.dto.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ApiResponse<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
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
            
            ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", jwt)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(maxAge)
                    .build();
            
            ResponseCookie idCookie = ResponseCookie.from("id", String.valueOf(user.getId()))
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(maxAge)
                    .build();
            
            ResponseCookie nameCookie = ResponseCookie.from("name", user.getFirstname() + "%20" + user.getLastname())
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(maxAge)
                    .build();
            
            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, idCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, nameCookie.toString());
            
            return new ApiResponse<>(null, "Login successful", HttpStatus.OK.value());
        } catch (BadCredentialsException e) {
            ResponseCookie clearCookie = ResponseCookie.from("jwtToken", "")
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(0)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());
            return new ApiResponse<>(null, "Invalid email or password", HttpStatus.UNAUTHORIZED.value());
        } catch (Exception e) {
            ResponseCookie clearCookie = ResponseCookie.from("jwtToken", "")
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(0)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());
            e.printStackTrace();
            return new ApiResponse<>(null, "Server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @GetMapping("/logout")
    public ApiResponse<?> logout(HttpServletResponse response) {
        ResponseCookie nameCookie = ResponseCookie.from("name", "")
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie idCookie = ResponseCookie.from("id", "")
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie jwtToken = ResponseCookie.from("jwtToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, nameCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, idCookie.toString());

        return new ApiResponse<>(null, "Logged out successfully", 200);
    }
}