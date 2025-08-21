package com.project.hospitalReport.controller;

import com.project.hospitalReport.dto.ApiResponse;
import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.entity.MedicalHistory;
import com.project.hospitalReport.entity.Patient;
import com.project.hospitalReport.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    HistoryService historyService;

    @PostMapping("/add")
    public ApiResponse<MedicalHistory> addMedicalHistory(@RequestBody MedicalHistory history, @CookieValue(value = "id") Long doctor_id) {
        Patient patient = historyService.getPatientById(history.getId());
        if (patient == null)
            return new ApiResponse<>(null, "No Patient Found!!!", HttpStatus.NO_CONTENT.value());
        Doctor doctor = historyService.getDoctorById(doctor_id);
        if (doctor == null)
            return new ApiResponse<>(null, "No Doctor Found!!!", HttpStatus.NO_CONTENT.value());
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setDiagnosis(history.getDiagnosis());
        medicalHistory.setDiagnosisDate(history.getDiagnosisDate());
        medicalHistory.setReview(history.getReview());
        medicalHistory.setRevisitDate(history.getRevisitDate());
        medicalHistory.setPatient(patient);
        medicalHistory.setDoctor(doctor);
        historyService.addHistory(medicalHistory);
        return new ApiResponse<>(null, "Medical History Added", HttpStatus.OK.value());
    }
}
