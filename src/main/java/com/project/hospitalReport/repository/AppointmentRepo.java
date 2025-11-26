package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDiagnosisDateAndDoctorIdAndIsConsultedFalse(LocalDate diagnosisDate, Long doctorId);

    List<Appointment> findByDiagnosisDateAndDoctorIdAndIsConsultedTrue(LocalDate diagnosisDate, Long doctorId);

    Integer countByDiagnosisDateAndDoctorIdAndIsConsultedFalse(LocalDate diagnosisDate, Long doctorId);

    Integer countByDiagnosisDateAndDoctorId(LocalDate diagnosisDate, Long doctorId);

}
