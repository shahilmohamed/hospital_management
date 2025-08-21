package com.project.hospitalReport.service;

import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.entity.MedicalHistory;
import com.project.hospitalReport.entity.Patient;
import com.project.hospitalReport.repository.DoctorRepo;
import com.project.hospitalReport.repository.HistoryRepo;
import com.project.hospitalReport.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    @Autowired
    HistoryRepo historyRepo;

    @Autowired
    PatientRepo patientRepo;

    @Autowired
    DoctorRepo doctorRepo;

    public Patient getPatientById(Long id) {
        return patientRepo.getById(id);
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepo.getById(id);
    }

    public MedicalHistory addHistory(MedicalHistory history) {
        return historyRepo.save(history);
    }
}
