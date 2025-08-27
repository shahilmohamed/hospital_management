package com.project.hospitalReport.service;

import com.project.hospitalReport.dto.MedicalHistoryRequest;
import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.entity.MedicalHistory;
import com.project.hospitalReport.entity.Patient;
import com.project.hospitalReport.entity.Prescription;
import com.project.hospitalReport.repository.DoctorRepo;
import com.project.hospitalReport.repository.HistoryRepo;
import com.project.hospitalReport.repository.PatientRepo;
import com.project.hospitalReport.repository.PrescriptionRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    @Autowired
    HistoryRepo historyRepo;

    @Autowired
    PatientRepo patientRepo;

    @Autowired
    DoctorRepo doctorRepo;

    @Autowired
    PrescriptionRepo prescriptionRepo;

    public Patient getPatientById(Long id) {
        return patientRepo.getById(id);
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepo.getById(id);
    }

    @Transactional
    public MedicalHistory addHistory(MedicalHistoryRequest request, Doctor doctor) {
        MedicalHistory mh = new MedicalHistory();
        mh.setDiagnosis(request.getDiagnosis());
        mh.setReview(request.getReview());
        mh.setDiagnosisDate(request.getDiagnosisDate());
        mh.setRevisitDate(request.getRevisitDate());
        mh.setPatient(request.getPatient());
        mh.setDoctor(doctor);

        MedicalHistory savedHistory = historyRepo.save(mh);

        List<Prescription> medicines = request.getPrescriptions().stream().map(med -> {
            Prescription prescription = new Prescription();
            prescription.setMedicalHistory(savedHistory);
            prescription.setStocks(med.getStock());
            prescription.setDosageMorning(med.getDosageMorning());
            prescription.setDosageAfternoon(med.getDosageAfternoon());
            prescription.setDosageNight(med.getDosageNight());
            prescription.setDurationDays(med.getDurationDays());
            return prescription;
        }).collect(Collectors.toList());
        prescriptionRepo.saveAll(medicines);

        return savedHistory;
    }

    public List<MedicalHistory> searchMedicalHistory(Long doctor_id, Long patient_id) {
        return historyRepo.searchMedicalHistory(doctor_id, patient_id);
    }

    public MedicalHistory getHistoryById(Long id) {
        return historyRepo.getById(id);
    }

}
