package com.project.hospitalReport.service;

import com.project.hospitalReport.entity.Appointment;
import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.entity.Patient;
import com.project.hospitalReport.repository.AppointmentRepo;
import com.project.hospitalReport.repository.DoctorRepo;
import com.project.hospitalReport.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepo appointmentRepo;

    @Autowired
    PatientRepo patientRepo;

    @Autowired
    DoctorRepo doctorRepo;

    public Patient getPatientById(Long id){
        return patientRepo.getById(id);
    }

    public Appointment addAppointment(Appointment appointment) {
        return appointmentRepo.save(appointment);
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepo.getById(id);
    }

    public List<Appointment> getAppointments(LocalDate diagnosisDate, Long doctorId) {
        return appointmentRepo.getAppointments(diagnosisDate, doctorId);
    }
}
