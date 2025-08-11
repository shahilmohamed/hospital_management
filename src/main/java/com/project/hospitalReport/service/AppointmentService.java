package com.project.hospitalReport.service;

import com.project.hospitalReport.entity.Appointment;
import com.project.hospitalReport.entity.Patient;
import com.project.hospitalReport.repository.AppointmentRepo;
import com.project.hospitalReport.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepo appointmentRepo;

    @Autowired
    PatientRepo patientRepo;

    public Patient getById(Long id){
        return patientRepo.getById(id);
    }
    public Appointment addAppointment(Appointment appointment){
        return appointmentRepo.save(appointment);
    }
}
