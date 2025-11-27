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
    PatientService patientService;

    @Autowired
    DoctorServiceV2 doctorServiceV2;

    public Patient getPatientById(Long id){
        return patientService.getPatientById(id);
    }

    public Appointment addAppointment(Appointment appointment) {
        return appointmentRepo.save(appointment);
    }

    public Doctor getDoctorById(Long id) {
        return doctorServiceV2.getById(id);
    }

    public List<Appointment> getAppointments(LocalDate diagnosisDate, Long doctorId) {
        return appointmentRepo.getAppointments(diagnosisDate, doctorId);
    }

    public List<Appointment> getConsultedAppointments(LocalDate diagnosisDate, Long doctorId) {
        return appointmentRepo.getConsultedAppointments(diagnosisDate, doctorId);
    }

    public String updateAppointment(Appointment data) {
        Appointment appointment = appointmentRepo.getReferenceById(data.getId());
        if (appointment != null) {
            appointment.setFirstname(data.getFirstname());
            appointment.setLastname(data.getLastname());
            appointment.setContactNumber(data.getContactNumber());
            appointment.setDiagnosis(data.getDiagnosis());
            appointment.setDiagnosisDate(data.getDiagnosisDate());
            appointment.setIsConsulted(data.getIsConsulted());
            appointmentRepo.save(appointment);
            return "Appointment Updated Successfully!!!";
        }
        return "No Appointments found!!!";
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepo.getById(id);
    }

}
