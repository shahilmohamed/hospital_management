package com.project.hospitalReport.controller;

import com.project.hospitalReport.dto.ApiResponse;
import com.project.hospitalReport.dto.MedicalHistoryRequest;
import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.entity.MedicalHistory;
import com.project.hospitalReport.entity.Patient;
import com.project.hospitalReport.service.HistoryService;
import com.project.hospitalReport.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    HistoryService historyService;

    @Autowired
    private SecurityService securityService;

    @PostMapping("/addHistory")
    public ApiResponse<MedicalHistory> addMedicalHistory(@RequestBody MedicalHistoryRequest history) {
        Long doctor_id = securityService.getCurrentDoctorId();
        Patient patient = historyService.getPatientById(history.getPatient().getId());
        if (patient == null)
            return new ApiResponse<>(null, "No Patient Found!!!", HttpStatus.NO_CONTENT.value());
        Doctor doctor = historyService.getDoctorById(doctor_id);
        if (doctor == null)
            return new ApiResponse<>(null, "No Doctor Found!!!", HttpStatus.NO_CONTENT.value());
        MedicalHistory h = historyService.addHistory(history, doctor);
        if (h != null)
            return new ApiResponse<>(null, "Medical History Added", HttpStatus.OK.value());
        else
            return new ApiResponse<>(null, "Can't Add Medical History!!!", HttpStatus.NO_CONTENT.value());
    }

    @PostMapping("/getHistory")
    public ApiResponse<List<Map<String, Object>>> searchMedicalHistory(@RequestBody Patient patient) {
        Long doctor_id = securityService.getCurrentDoctorId();
        List<MedicalHistory> history = historyService.searchMedicalHistory(doctor_id, patient.getId());
        ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>();
        List<Map<String, Object>> result = new ArrayList<>();
        if (history.size() > 0) {
            for (MedicalHistory h : history) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", h.getId());
                map.put("diagnosis", h.getDiagnosis());
                map.put("diagnosisDate", h.getDiagnosisDate());
                map.put("review", h.getReview());
                map.put("revisitDate", h.getRevisitDate());
                map.put("doctor_id", h.getDoctor().getId());
                map.put("patient_id", h.getPatient().getId());
                result.add(map);
            }
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Medical History Found");
            response.setData(result);
            return response;
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setMessage("No Medical History Found!!!");
            return response;
        }
    }

}
