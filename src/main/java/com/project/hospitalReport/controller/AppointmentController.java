package com.project.hospitalReport.controller;

import com.project.hospitalReport.dto.ApiResponse;
import com.project.hospitalReport.entity.Appointment;
import com.project.hospitalReport.entity.Patient;
import com.project.hospitalReport.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @PostMapping("/add")
    public ApiResponse<Appointment> addAppointment(@RequestBody Appointment appointment){
        ApiResponse<Appointment> response = new ApiResponse<>();
        Patient patient = appointmentService.getById(appointment.getId());
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
            System.out.println(a.toString());
            appointmentService.addAppointment(a);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Appointment Added Successfully");
            response.setData(a);
            return response;
        } catch (Exception e) {
            return new ApiResponse<>(null, e.toString(), HttpStatus.NO_CONTENT.value());
        }

    }
}
