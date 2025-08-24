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

    @Query(value = "SELECT a.id, a.contactNumber, a.diagnosis, a.diagnosisDate, a.firstname, a.isConsulted, a.lastname, a.doctor_id, a.patient_id " +
            "FROM appointment AS a WHERE diagnosisDate = :diagnosisDate " +
            "AND a.doctor_id = :doctorId " +
            "AND a.isConsulted =0;", nativeQuery = true)
    List<Appointment> getAppointments(@Param("diagnosisDate") LocalDate diagnosisDate, @Param("doctorId") Long doctorId);

    @Query(value = "SELECT a.id, a.contactNumber, a.diagnosis, a.diagnosisDate, a.firstname, a.isConsulted, a.lastname, a.doctor_id, a.patient_id " +
            "FROM appointment AS a WHERE diagnosisDate = :diagnosisDate " +
            "AND a.doctor_id = :doctorId " +
            "AND a.isConsulted =1;", nativeQuery = true)
    List<Appointment> getConsultedAppointments(@Param("diagnosisDate") LocalDate diagnosisDate, @Param("doctorId") Long doctorId);

}
