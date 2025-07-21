package com.project.hospitalReport.service;

import com.project.hospitalReport.repository.PatientRepo;
import com.project.hospitalReport.entity.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    PatientRepo patientRepo;

    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    public Patient getPatientById(Long id)
    {
        return patientRepo.getById(id);
    }

    public Optional<Patient> findByFirstnameAndLastnameAndGenderAndContactNumberAndBloodGroupAndDob(String firstname, String lastname, String gender, String contactNumber, String bloodGroup, Date dob)
    {
        return patientRepo.findByFirstnameAndLastnameAndGenderAndContactNumberAndBloodGroupAndDob(firstname, lastname, gender, contactNumber, bloodGroup, dob);
    }

    public int isMappingExists(Long patient_id, Long doctor_id)
    {
        return patientRepo.isMappingExists(patient_id,doctor_id);
    }

    public int insertPatient(Long patient_id, Long doctor_id)
    {
        return patientRepo.insertPatientDoctor(patient_id, doctor_id);
    }

    public Patient addPatient(Patient patient)
    {
        return patientRepo.save(patient);
    }
}
