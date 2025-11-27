package com.project.hospitalReport.service;

import com.project.hospitalReport.dto.PageRequ;
import com.project.hospitalReport.repository.PatientRepo;
import com.project.hospitalReport.entity.Patient;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public List<Patient> getPatientByDoctorId(Long id)
    {
        return patientRepo.findPatientByDoctorId(id);
    }

    public List<Patient> searchPatient(Long doctorId, Patient patient)
    {
        return patientRepo.searchPatient(doctorId, patient.getContactNumber(), patient.getId());
    }

    public String upadatePatient(Patient request)
    {
        Patient patient = patientRepo.getById(request.getId());
        if (patient!=null)
        {
            patient.setFirstname(request.getFirstname());
            patient.setLastname(request.getLastname());
            patient.setAddress(request.getAddress());
            patient.setDob(request.getDob());
            patient.setBloodGroup(request.getBloodGroup());
            patient.setContactNumber(request.getContactNumber());
            patient.setGender(request.getGender());
            patientRepo.save(patient);
            return "Patient updated successfully!!!";
        }
        return "No Patients Found!!?";
    }

    public String deletePatient(Patient request)
    {
         Patient patient = patientRepo.getById(request.getId());
         try
         {
             patient.getFirstname();
             patientRepo.deleteById(request.getId());
             return "Patient Deleted Successfully";
         } catch (EntityNotFoundException e) {
             return "Can't Find Patient To Delete";
         }
    }

    public Page<Patient> getPatientByDoctorIdPage(Long id, PageRequ pageRequ)
    {
        Pageable pageable = PageRequest.of(pageRequ.getPage(), pageRequ.getSize());
        return patientRepo.findPatientByDoctorIdPage(id, pageable);
    }

    public  Page<Patient> findPatientByDoctorIdAndName(Long id, PageRequ pageRequ)
    {
        Pageable pageable = PageRequest.of(pageRequ.getPage(), pageRequ.getSize());
        return patientRepo.findPatientByDoctorIdAndName(id, pageRequ.getSearch(), pageable);
    }
}
