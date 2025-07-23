package com.project.hospitalReport.service;

import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.repository.DoctorRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DoctorServiceV2 implements UserDetailsService {

    @Autowired
    DoctorRepo doctorRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Doctor getById(Long id)
    {
        return doctorRepo.getById(id);
    }

    public String registerUser(Doctor doctor)
    {
        if (doctorRepo.existsByEmail(doctor.getEmail()))
            return "User already exists";
        Doctor d = new Doctor();
        d.setFirstname(doctor.getFirstname());
        d.setLastname(doctor.getLastname());
        d.setEmail(doctor.getEmail());
        d.setPassword(passwordEncoder.encode(doctor.getPassword()));
        d.setDob(doctor.getDob());
        d.setDob(doctor.getDob());
        d.setGender(doctor.getGender());
        d.setRole(doctor.getRole());
        d.setPhoneNumber(doctor.getPhoneNumber());
        d.setSpecialization(doctor.getSpecialization());
        doctorRepo.save(d);
        return "Signup successful";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Doctor doctor = doctorRepo.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(doctor.getEmail())
                .password(doctor.getPassword())
                .build();
    }
}
