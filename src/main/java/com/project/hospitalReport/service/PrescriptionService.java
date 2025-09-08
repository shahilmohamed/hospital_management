package com.project.hospitalReport.service;

import com.project.hospitalReport.entity.Prescription;
import com.project.hospitalReport.repository.PrescriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {

    @Autowired
    PrescriptionRepo prescriptionRepo;

    public List<Prescription> getPrescription(Long medicalHistoryId) {
        return prescriptionRepo.getPrescription(medicalHistoryId);
    }
}
