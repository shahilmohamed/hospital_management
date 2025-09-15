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

    @PostMapping("/addPatient")
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
                    existing.get().setDoctors(null);
                    response.setData(null);
                    return response;
                } else {
                    patientService.insertPatient(patientId, doctor_id);
                    response.setStatus(HttpStatus.OK.value());
                    response.setMessage("Doctor Added to Patient Successfully.");
                    patient.setId(existing.get().getId());
                    patient.setDoctors(null);
                    response.setData(null);
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
                p.setDoctors(null);
                response.setData(null);
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

    @PostMapping("searchPatient")
    public ApiResponse<List<Map<String, Object>>> searchPatient(@RequestBody Patient patient, @CookieValue(value = "id") Long doctor_id)
    {
        List<Patient> patients = patientService.searchPatient(doctor_id, patient);
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

    @PutMapping("/updatePatient")
    public ApiResponse<String> updatePatient(@RequestBody Patient patient)
    {
        String result = patientService.upadatePatient(patient);
        ApiResponse<String> response = new ApiResponse<>(null, result,HttpStatus.OK.value());
        return response;
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deletePatient(@RequestBody Patient patient)
    {
        String result = patientService.deletePatient(patient);
        ApiResponse<String> response = new ApiResponse<>();
        if (result.equals("Patient Deleted Successfully"))
        {
            response.setData(null);
            response.setMessage(result);
            response.setStatus(HttpStatus.OK.value());
        }
        else
        {
            response.setData(null);
            response.setMessage(result);
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
        return response;
    }

    @GetMapping("/getPatientById/{id}")
    public ApiResponse<Map<String, Object>> particularPatient(@PathVariable Long id) {
        Patient p = patientService.getPatientById(id);
        ApiResponse<Map<String, Object>> response = new ApiResponse<>();
        if (p != null) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", p.getId());
            map.put("firstname", p.getFirstname());
            map.put("lastname", p.getLastname());
            map.put("address", p.getAddress());
            map.put("bloodGroup", p.getBloodGroup());
            map.put("contactNumber", p.getContactNumber());
            map.put("gender", p.getGender());
            map.put("dob", p.getDob());
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Patients found");
            response.setData(map);
            return response;
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setMessage("No Patients found!!!");
            response.setData(null);
            return response;
        }
    }

}