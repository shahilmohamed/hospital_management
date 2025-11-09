package com.project.hospitalReport.controller;

import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.service.DoctorServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/doctors")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
public class DoctorControllerV2 {

    @Autowired
    DoctorServiceV2 doctorServiceV2;

    @GetMapping("/getDoctorById/{id}")
    public ResponseEntity<Map<String, Object>> getDoctorById(@PathVariable Long id){
        Doctor doctor = doctorServiceV2.getById(id);

        if (!doctor.getFirstname().isEmpty()){
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> data = new HashMap<>();
            data.put("id", doctor.getId());
            data.put("firstname", doctor.getFirstname());
            data.put("lastname", doctor.getLastname());
            data.put("dob", doctor.getDob());
            data.put("gender", doctor.getGender());
            data.put("role", doctor.getRole());
            data.put("specialization", doctor.getSpecialization());
            data.put("email", doctor.getEmail());
            data.put("phoneNumber", doctor.getPhoneNumber());
            data.put("password", "**********");
            list.add(data);
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Doctor Found");
            response.put("data", list);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(Map.of(
                "status", HttpStatus.NO_CONTENT.value(),
                "message", "No Doctor Found!!!",
                "data", Collections.emptyList()
//                "total Count", 0,
//                "total Pages", 0
        ));

    }
}
