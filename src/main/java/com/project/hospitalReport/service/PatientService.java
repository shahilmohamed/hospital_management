package com.project.hospitalReport.service;

import com.project.hospitalReport.repository.PatientRepo;
import com.project.hospitalReport.entity.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    PatientRepo patientRepo;

    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    public Patient addPatient(Patient patient)
    {
        return patientRepo.save(patient);
    }
}
