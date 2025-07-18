package com.project.hospitalReport.controller;

import com.project.hospitalReport.entity.Patient;
import com.project.hospitalReport.service.ApiResponse;
import com.project.hospitalReport.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
@RequestMapping("patients")
public class PatientController {

    @Autowired
    private PatientService service;

    @GetMapping("/getAll")
    public ApiResponse<List<Map<String, Object>>> getPatient() {
        List<Patient> patients = service.getAllPatients();
        ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>();
        List<Map<String, Object>> result = new ArrayList<>();
        if (patients.size() > 0) {
            for (Patient p : patients) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", p.getId());
                map.put("name", p.getFirstname() + " " + p.getLastname());
                map.put("address", p.getAddress());
                map.put("bloodGroup", p.getBloodGroup());
                map.put("contactNumber", p.getContactNumber());
                map.put("gender", p.getGender());
                map.put("dob", p.getDob());
                result.add(map);
            }
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Patients found");
            response.setData(result);
            return response;
        } else {
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("No Patients found!!!");
            return response;
        }
    }

}