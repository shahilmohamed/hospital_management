package com.project.hospitalReport.service;

import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceV2 {

    @Autowired
    DoctorRepo doctorRepo;

    public Doctor getById(Long id)
    {
        return doctorRepo.getById(id);
    }
}
