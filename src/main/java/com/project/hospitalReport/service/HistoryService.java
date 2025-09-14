package com.project.hospitalReport.service;

import com.project.hospitalReport.dto.MedicalHistoryRequest;
import com.project.hospitalReport.entity.*;
import com.project.hospitalReport.repository.*;
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
    PatientService patientService;

    @Autowired
    DoctorServiceV2 doctorServiceV2;

    @Autowired
    PrescriptionService prescriptionService;

    @Autowired
    AppointmentService appointmentService;

    public Patient getPatientById(Long id) {
        return patientService.getPatientById(id);
    }

    public Doctor getDoctorById(Long id) {
        return doctorServiceV2.getById(id);
    }

    @Transactional
    public MedicalHistory addHistory(MedicalHistoryRequest request, Doctor doctor) {
        Appointment appointment = appointmentService.getAppointmentById(request.getAppointment_id());
        if (!appointment.getIsConsulted()) {
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
            prescriptionService.saveAllPrescription(medicines);

            appointment.setIsConsulted(true);
            appointmentService.addAppointment(appointment);

            return savedHistory;
        } else {
            return null;
        }
    }

    public List<MedicalHistory> searchMedicalHistory(Long doctor_id, Long patient_id) {
        return historyRepo.searchMedicalHistory(doctor_id, patient_id);
    }

    public MedicalHistory getHistoryById(Long id) {
        return historyRepo.getById(id);
    }

}
