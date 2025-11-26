package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.Patient;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
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


    @Query(value = "SELECT p.id, p.firstname, p.lastname, p.dob, p.contactNumber, p.address, p.bloodGroup, p.gender " +
            "FROM patient p " +
            "LEFT JOIN patient_doctor pd ON p.id = pd.patient_id " +
            "WHERE (p.contactNumber = :contactNumber OR p.id = :id) " +
            "AND pd.doctor_id= :userId;", nativeQuery = true)
    List<Patient> searchPatient(@Param("userId") Long doctorId, @Param("contactNumber") String contactNumber, @Param("id") Long id);

    @Query("SELECT p FROM Patient p JOIN p.doctors d WHERE d.id = :doctorId")
    List<Patient> findPatientByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT p FROM Patient p JOIN p.doctors d WHERE d.id = :doctorId")
    Page<Patient> findPatientByDoctorId(@Param("doctorId") Long doctorId, Pageable pageable);

    @Query("SELECT p FROM Patient p JOIN p.doctors d WHERE d.id = :doctorId " +
           "AND (LOWER(p.firstname) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(p.lastname) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Patient> findPatientByDoctorIdAndName(@Param("doctorId") Long doctorId, @Param("search") String search, Pageable pageable);

}
