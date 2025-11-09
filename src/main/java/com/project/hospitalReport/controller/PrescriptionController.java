package com.project.hospitalReport.controller;


import com.project.hospitalReport.dto.ApiResponse;
import com.project.hospitalReport.entity.DrugsStock;
import com.project.hospitalReport.entity.MedicalHistory;
import com.project.hospitalReport.entity.Patient;
import com.project.hospitalReport.entity.Prescription;
import com.project.hospitalReport.service.DrugsService;
import com.project.hospitalReport.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
@RequestMapping("/prescription")
public class PrescriptionController {

    @Autowired
    PrescriptionService prescriptionService;

    @Autowired
    DrugsService drugsService;

    @PostMapping("/getPrescription")
    public ApiResponse<List<Map<String, Object>>> getPrescription(@RequestBody MedicalHistory history) {
        List<Prescription> prescriptions = prescriptionService.getPrescription(history.getId());
        ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>();
        List<Map<String, Object>> result = new ArrayList<>();
        if (prescriptions.size() > 0) {
            for (Prescription p : prescriptions) {
                DrugsStock stock = drugsService.getById(p.getStocks().getId());
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", p.getId());
                map.put("name", stock.getName());
                map.put("durationDays", p.getDurationDays());
                map.put("dosageMorning", p.getDosageMorning());
                map.put("dosageAfternoon", p.getDosageAfternoon());
                map.put("dosageNight", p.getDosageNight());
                result.add(map);
            }
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Prescriptions Found");
            response.setData(result);
            return response;
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setMessage("No Prescriptions Found!!!");
            return response;
        }
    }

}
