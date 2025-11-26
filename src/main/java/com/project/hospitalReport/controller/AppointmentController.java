package com.project.hospitalReport.controller;

import com.project.hospitalReport.dto.ApiResponse;
import com.project.hospitalReport.entity.Appointment;
import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.entity.Patient;
import com.project.hospitalReport.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @PostMapping("/addAppointment")
    public ApiResponse<Appointment> addAppointment(@RequestBody Appointment appointment, @CookieValue(value = "id") Long doctor_id) {
        ApiResponse<Appointment> response = new ApiResponse<>();
        Patient patient = appointmentService.getPatientById(appointment.getId());
        Doctor doctor = appointmentService.getDoctorById(doctor_id);
        if (patient == null)
            return new ApiResponse<>(null, "No Patient Found", HttpStatus.NO_CONTENT.value());
        try {
            Appointment a = new Appointment();
            a.setFirstname(appointment.getFirstname());
            a.setLastname(appointment.getLastname());
            a.setContactNumber(appointment.getContactNumber());
            a.setDiagnosis(appointment.getDiagnosis());
            a.setDiagnosisDate(appointment.getDiagnosisDate());
            a.setIsConsulted(appointment.getIsConsulted());
            a.setPatient(patient);
            a.setDoctor(doctor);
            appointmentService.addAppointment(a);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Appointment Added Successfully");
            response.setData(null);
            return response;
        } catch (Exception e) {
            return new ApiResponse<>(null, e.toString(), HttpStatus.NO_CONTENT.value());
        }

    }

    @PostMapping("/getAppointment")
    public ApiResponse<List<Map<String, Object>>> getAppointments(@RequestBody Appointment appointment, @CookieValue(value = "id") Long doctor_id) {
        List<Appointment> appointments = appointmentService.getAppointments(appointment.getDiagnosisDate(), doctor_id);
        ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>();
        List<Map<String, Object>> result = new ArrayList<>();
        if (appointments.size() > 0) {
            for (Appointment a : appointments) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", a.getId());
                map.put("contactNumber", a.getContactNumber());
                map.put("diagnosis", a.getDiagnosis());
                map.put("diagnosisDate", a.getDiagnosisDate());
                map.put("firstname", a.getFirstname());
                map.put("isConsulted", a.getIsConsulted());
                map.put("lastname", a.getLastname());
                map.put("doctor_id", a.getDoctor().getId());
                map.put("patient_id", a.getPatient().getId());
                result.add(map);
            }
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Appointments found");
            response.setData(result);
            return response;
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setMessage("No Appointments found!!!");
            response.setData(result);
            return response;
        }
    }

    @PostMapping("/getConsultedAppointments")
    public ApiResponse<List<Map<String, Object>>> getConsultedAppointments(@RequestBody Appointment appointment, @CookieValue(value = "id") Long doctor_id) {
        List<Appointment> appointments = appointmentService.getConsultedAppointments(appointment.getDiagnosisDate(), doctor_id);
        ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>();
        List<Map<String, Object>> result = new ArrayList<>();
        if (appointments.size() > 0) {
            for (Appointment a : appointments) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", a.getId());
                map.put("contactNumber", a.getContactNumber());
                map.put("diagnosis", a.getDiagnosis());
                map.put("diagnosisDate", a.getDiagnosisDate());
                map.put("firstname", a.getFirstname());
                map.put("isConsulted", a.getIsConsulted());
                map.put("lastname", a.getLastname());
                map.put("doctor_id", a.getDoctor().getId());
                map.put("patient_id", a.getPatient().getId());
                result.add(map);
            }
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Appointments found");
            response.setData(result);
            return response;
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.setMessage("No Appointments found!!!");
            return response;
        }
    }

    @PutMapping("/updateAppointment")
    public ApiResponse<String> updateAppointment(@RequestBody Appointment appointment) {
        String result = appointmentService.updateAppointment(appointment);
        ApiResponse<String> response = new ApiResponse<>(null, result, HttpStatus.OK.value());
        return response;
    }

    @PostMapping("/getAppointmentById")
    public ApiResponse<Map<String, Object>> getAppointmentById(@RequestBody Appointment appointment)
    {
        ApiResponse<Map<String, Object>> response = new ApiResponse<>();
        Appointment a = appointmentService.getAppointmentById(appointment.getId());
        if (a != null)
        {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", a.getId());
            map.put("contactNumber", a.getContactNumber());
            map.put("diagnosis", a.getDiagnosis());
            map.put("diagnosisDate", a.getDiagnosisDate());
            map.put("firstname", a.getFirstname());
            map.put("isConsulted", a.getIsConsulted());
            map.put("lastname", a.getLastname());
            map.put("doctor_id", a.getDoctor().getId());
            map.put("patient_id", a.getPatient().getId());
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Appointments found");
            response.setData(map);
            return response;
        }
        response.setStatus(HttpStatus.NO_CONTENT.value());
        response.setMessage("No Appointments found!!!");
        return response;
    }

    @PostMapping("/getAppointmentCount")
    public ResponseEntity<Map<String, Object>> getAppointmentCount(@RequestBody Appointment appointment, @CookieValue(value = "id") Long doctor_id){
        Integer todayAppointmentCount = appointmentService.todayAppointmentCount(appointment.getDiagnosisDate(), doctor_id);
        Integer pendingAppointmentCount = appointmentService.pendingAppointmentCount(appointment.getDiagnosisDate(), doctor_id);
        Integer consultedAppointmentCount = todayAppointmentCount - pendingAppointmentCount;

        System.out.println("Today appointment: "+todayAppointmentCount);
        System.out.println("Pending appointment: "+pendingAppointmentCount);
        System.out.println("Consulted appointment: "+consultedAppointmentCount);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("todays_appointment",todayAppointmentCount);
        data.put("pending_appointment", pendingAppointmentCount);
        data.put("consulted_appointment", consultedAppointmentCount);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Appointment count found");
        response.put("data", data);
        return ResponseEntity.ok(response);

    }

}
