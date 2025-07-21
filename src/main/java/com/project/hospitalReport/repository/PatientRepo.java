package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.Patient;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Long> {

    Optional<Patient> findByFirstnameAndLastnameAndGenderAndContactNumberAndBloodGroupAndDob(String firstname, String lastname, String gender, String contactNumber, String bloodGroup, Date dob);
    @Query(value = "SELECT COUNT(*) FROM patient_doctor WHERE patient_id = :patientId AND doctor_id = :doctorId", nativeQuery = true)
    int isMappingExists(@Param("patientId") Long patientId, @Param("doctorId") Long doctorId);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO patient_doctor (patient_id, doctor_id) VALUES (:patientId, :doctorId)", nativeQuery = true)
    int insertPatientDoctor(@Param("patientId") Long patientId, @Param("doctorId") Long doctorId);
}
