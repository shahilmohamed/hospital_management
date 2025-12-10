package com.project.hospitalReport.service;

import com.project.hospitalReport.dto.PageRequ;
import com.project.hospitalReport.entity.Appointment;
import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.entity.Patient;
import com.project.hospitalReport.repository.AppointmentRepo;
import com.project.hospitalReport.repository.AppointmentSpecifications;
import com.project.hospitalReport.repository.DoctorRepo;
import com.project.hospitalReport.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
        return appointmentRepo.findByDiagnosisDateAndDoctorIdAndIsConsultedFalse(diagnosisDate, doctorId);
    }

    public List<Appointment> getConsultedAppointments(LocalDate diagnosisDate, Long doctorId) {
        return appointmentRepo.findByDiagnosisDateAndDoctorIdAndIsConsultedTrue(diagnosisDate, doctorId);
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

    public Page<Appointment> findUpcomingAppointments(Long id, PageRequ pageRequ) {
        Pageable pageable = PageRequest.of(pageRequ.getPage(), pageRequ.getSize());
        return appointmentRepo.findByDiagnosisDateAndDoctorIdAndIsConsultedFalse(pageRequ.getDate(), id, pageable);
    }

    public Page<Appointment> findUpcomingAppointmentsWithSearch(Long doctorId, PageRequ pageRequ) {
        Pageable pageable = PageRequest.of(pageRequ.getPage(), pageRequ.getSize());
        Specification<Appointment> spec = AppointmentSpecifications.findByDiagnosisDateDoctorIdAndIsConsultedFalseAndNameContaining(
                LocalDate.now(), doctorId, pageRequ.getSearch());
        return appointmentRepo.findAll(spec, pageable);
    }

    public Page<Appointment> findConsultedAppointments(Long id, PageRequ pageRequ) {
        Pageable pageable = PageRequest.of(pageRequ.getPage(), pageRequ.getSize());
        return appointmentRepo.findByDiagnosisDateAndDoctorIdAndIsConsultedTrue(pageRequ.getDate(), id, pageable);
    }

    public Page<Appointment> findConsultedAppointmentsWithSearch(Long doctorId, PageRequ pageRequ) {
        Pageable pageable = PageRequest.of(pageRequ.getPage(), pageRequ.getSize());
        Specification<Appointment> spec = AppointmentSpecifications.findByDiagnosisDateDoctorIdAndIsConsultedTrueAndNameContaining(
                pageRequ.getDate(), doctorId, pageRequ.getSearch());
        return appointmentRepo.findAll(spec, pageable);
    }
}
