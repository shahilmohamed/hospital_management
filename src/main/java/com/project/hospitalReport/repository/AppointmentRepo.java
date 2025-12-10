package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    List<Appointment> findByDiagnosisDateAndDoctorIdAndIsConsultedFalse(LocalDate diagnosisDate, Long doctorId);

    List<Appointment> findByDiagnosisDateAndDoctorIdAndIsConsultedTrue(LocalDate diagnosisDate, Long doctorId);

    Page<Appointment> findByDiagnosisDateAndDoctorIdAndIsConsultedTrue(LocalDate diagnosisDate, Long doctorId, Pageable pageable);

    Page<Appointment> findByDiagnosisDateAndDoctorIdAndIsConsultedFalse(LocalDate diagnosisDate, Long doctorId, Pageable pageable);
}
