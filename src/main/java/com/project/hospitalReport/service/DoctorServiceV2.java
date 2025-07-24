package com.project.hospitalReport.service;

import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.repository.DoctorRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DoctorServiceV2{

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

}
