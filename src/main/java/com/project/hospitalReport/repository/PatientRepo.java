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

    @Query(value = "SELECT p.id, p.address, p.bloodGroup, p.contactNumber, p.firstname, p.gender, p.lastname, p.dob FROM patient_doctor pd JOIN patient p ON p.id = pd.patient_id WHERE pd.doctor_id= :doctorId", nativeQuery = true)
    List<Patient> findPatientByDoctorId(@Param("doctorId") Long doctorId);

    @Query(value = "SELECT p.id, p.firstname, p.lastname, p.dob, p.contactNumber, p.address, p.bloodGroup, p.gender " +
            "FROM patient p " +
            "LEFT JOIN patient_doctor pd ON p.id = pd.patient_id " +
            "WHERE (p.contactNumber = :contactNumber OR p.id = :id) " +
            "AND pd.doctor_id= :userId;", nativeQuery = true)
    List<Patient> searchPatient(@Param("userId") Long doctorId, @Param("contactNumber") String contactNumber, @Param("id") Long id);

    @Query(value = "SELECT p.id, p.address, p.bloodGroup, p.contactNumber, p.firstname, p.gender, p.lastname, p.dob FROM patient_doctor pd JOIN patient p ON p.id = pd.patient_id WHERE pd.doctor_id= :doctorId", nativeQuery = true)
    Page<Patient> findPatientByDoctorIdPage(@Param("doctorId") Long doctorId, Pageable pageable);

    @Query(
            value = "SELECT p.id, p.address, p.bloodGroup, p.contactNumber, p.firstname, p.gender, p.lastname, p.dob " +
                    "FROM patient_doctor pd " +
                    "JOIN patient p ON p.id = pd.patient_id " +
                    "WHERE pd.doctor_id = :doctorId " +
                    "AND (LOWER(p.firstname) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "     OR LOWER(p.lastname) LIKE LOWER(CONCAT('%', :search, '%')))",
            countQuery = "SELECT COUNT(*) " +
                    "FROM patient_doctor pd " +
                    "JOIN patient p ON p.id = pd.patient_id " +
                    "WHERE pd.doctor_id = :doctorId " +
                    "AND (LOWER(p.firstname) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "     OR LOWER(p.lastname) LIKE LOWER(CONCAT('%', :search, '%')))",
            nativeQuery = true
    )
    Page<Patient> findPatientByDoctorIdAndName(Long doctorId, String search, Pageable pageable);

}
