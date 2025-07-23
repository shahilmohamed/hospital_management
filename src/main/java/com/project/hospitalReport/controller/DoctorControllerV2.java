package com.project.hospitalReport.controller;

import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.jwtUtility.JwtUtil;
import com.project.hospitalReport.repository.DoctorRepo;
import com.project.hospitalReport.service.ApiResponse;
import com.project.hospitalReport.service.DoctorServiceV2;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
public class DoctorControllerV2 {

    @Autowired
    private DoctorRepo doctorRepo;

    @Autowired
    DoctorServiceV2 doctorServiceV2;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public String createDoctor(@RequestBody Doctor doctor)
    {
        return doctorServiceV2.registerUser(doctor);
    }

    @PostMapping("/login")
    public ApiResponse<String> login(HttpServletResponse res, @RequestBody Doctor doctor)
    {
        ApiResponse<String> response = new ApiResponse<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(doctor.getEmail(), doctor.getPassword())
            );
            String token = jwtUtil.generateToken(doctor.getEmail());
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(24*60*60);
            res.addCookie(cookie);
            response.setMessage("Login successful");
            response.setStatus(HttpStatus.OK.value());
            response.setData(token);
            return response;

        }
        catch (Exception e)
        {
            response.setMessage("Login failed");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setData(e.toString());
            return response;
        }
    }

}
