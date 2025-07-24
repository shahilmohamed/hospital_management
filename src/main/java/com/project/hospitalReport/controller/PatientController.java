package com.project.hospitalReport.controller;

import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.entity.Patient;
import com.project.hospitalReport.dto.ApiResponse;
import com.project.hospitalReport.service.DoctorServiceV2;
import com.project.hospitalReport.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorServiceV2 doctorServiceV2;

    @GetMapping("/getAll")
    public ApiResponse<List<Map<String, Object>>> getPatient() {
        List<Patient> patients = patientService.getAllPatients();
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

    @PostMapping("/add")
    public ApiResponse<Patient> addPatient(@RequestBody Patient patient, @CookieValue(name = "id") Long doctor_id) {
        ApiResponse<Patient> response = new ApiResponse<>();
        Doctor doctor = doctorServiceV2.getById(doctor_id);
        if (doctor != null) {
            Optional<Patient> existing = patientService.findByFirstnameAndLastnameAndGenderAndContactNumberAndBloodGroupAndDob(
                    patient.getFirstname(),
                    patient.getLastname(),
                    patient.getGender(),
                    patient.getContactNumber(),
                    patient.getBloodGroup(),
                    patient.getDob()
            );
            if (existing.isPresent()) {
                Long patientId = Long.valueOf(existing.get().getId());
                int exists = patientService.isMappingExists(patientId, doctor_id);
                if (exists > 0) {
                    response.setStatus(HttpStatus.OK.value());
                    response.setMessage("Patient Already Added!");
                    response.setData(existing.get());
                    return response;
                } else {
                    patientService.insertPatient(patientId, doctor_id);
                    response.setStatus(HttpStatus.OK.value());
                    response.setMessage("Doctor Added to Patient Successfully.");
                    patient.setId(existing.get().getId());
                    response.setData(patient);
                    return response;
                }
            } else if (!existing.isPresent()) {
                Patient p = new Patient();
                p.setFirstname(patient.getFirstname());
                p.setLastname(patient.getLastname());
                p.setAddress(patient.getAddress());
                p.setGender(patient.getGender());
                p.setDob(patient.getDob());
                p.setBloodGroup(patient.getBloodGroup());
                p.setContactNumber(patient.getContactNumber());
                p.getDoctors().add(doctor);
                doctor.getPatients().add(p);
                patientService.addPatient(p);
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("Patient Added Successfully");
                response.setData(p);
                return response;
            } else {
                response.setStatus(HttpStatus.CONTINUE.value());
                response.setMessage("Patient already exists.");
                response.setData(null);
                return response;
            }
        }
        response.setStatus(HttpStatus.CONTINUE.value());
        response.setMessage("Doctor doesn't exists.");
        response.setData(null);
        return response;
    }

    @GetMapping("getAllPatient")
    public ApiResponse<List<Map<String, Object>>> getPatientByDoctorId(@CookieValue(value = "id") Long doctor_id)
    {
        List<Patient> patients = patientService.getPatientByDoctorId(doctor_id);
        ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>();
        List<Map<String, Object>> result = new ArrayList<>();
        if (patients.size()>0)
        {
            for (Patient p : patients)
            {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", p.getId());
                map.put("firstname", p.getFirstname());
                map.put("lastname", p.getLastname());
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