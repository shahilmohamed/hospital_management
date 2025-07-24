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
import org.springframework.http.HttpStatus;
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

            Cookie cookie = new Cookie("jwtToken", jwt);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            Cookie id = new Cookie("id", String.valueOf(user.getId()));
            id.setPath("/");
            Cookie name = new Cookie("name", user.getFirstname() + "%20" + user.getLastname());
            name.setPath("/");
            response.addCookie(cookie);
            response.addCookie(id);
            response.addCookie(name);
            return new ApiResponse<>(null, "Login successful", HttpStatus.OK.value());
        } catch (BadCredentialsException e) {
            Cookie clearCookie = new Cookie("jwtToken", null);
            clearCookie.setHttpOnly(true);
            clearCookie.setMaxAge(0);
            clearCookie.setPath("/");
            response.addCookie(clearCookie);
            return new ApiResponse<>(null, "Invalid email or password", HttpStatus.UNAUTHORIZED.value());
        } catch (Exception e) {
            Cookie clearCookie = new Cookie("jwtToken", null);
            clearCookie.setHttpOnly(true);
            clearCookie.setMaxAge(0);
            clearCookie.setPath("/");
            response.addCookie(clearCookie);
            e.printStackTrace();
            return new ApiResponse<>(null, "Server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @GetMapping("/logout")
    public ApiResponse<?> logout(HttpServletResponse response) {
        Cookie nameCookie = new Cookie("name", "");
        nameCookie.setPath("/");
        nameCookie.setMaxAge(0);
        nameCookie.setHttpOnly(false);

        Cookie idCookie = new Cookie("id", "");
        idCookie.setPath("/");
        idCookie.setMaxAge(0);
        idCookie.setHttpOnly(false);

        Cookie jwtToken = new Cookie("jwtToken", "");
        jwtToken.setHttpOnly(true);
        jwtToken.setPath("/");
        jwtToken.setMaxAge(0); // Deletes cookie
        jwtToken.setHttpOnly(false);

        response.addCookie(jwtToken);
        response.addCookie(nameCookie);
        response.addCookie(idCookie);

        return new ApiResponse<>(null, "Logged out successfully", 200);
    }
}
