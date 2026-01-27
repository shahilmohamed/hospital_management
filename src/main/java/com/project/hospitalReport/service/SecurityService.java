package com.project.hospitalReport.service;

import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    @Autowired
    private DoctorRepo doctorRepo;

    /**
     * Gets the current authenticated doctor's ID from SecurityContext
     * @return Long doctor ID
     * @throws RuntimeException if user is not authenticated or doctor not found
     */
    public Long getCurrentDoctorId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String email = userDetails.getUsername();
            
            Optional<Doctor> doctor = doctorRepo.findByEmail(email);
            if (doctor.isPresent()) {
                return doctor.get().getId();
            } else {
                throw new RuntimeException("Doctor not found for email: " + email);
            }
        } else {
            throw new RuntimeException("Invalid authentication principal");
        }
    }

    /**
     * Gets the current authenticated doctor entity
     * @return Doctor entity
     * @throws RuntimeException if user is not authenticated or doctor not found
     */
    public Doctor getCurrentDoctor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String email = userDetails.getUsername();
            
            Optional<Doctor> doctor = doctorRepo.findByEmail(email);
            if (doctor.isPresent()) {
                return doctor.get();
            } else {
                throw new RuntimeException("Doctor not found for email: " + email);
            }
        } else {
            throw new RuntimeException("Invalid authentication principal");
        }
    }
}

