package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.MedicalHistory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepo extends JpaRepository<MedicalHistory, Long> {

    @Query(value = "select his.id, his.diagnosis, his.diagnosisDate, his.review, his.revisitDate, his.doctor_id, his.patient_id " +
            "from medicalhistory as his " +
            "where his.doctor_id = :doctorId and his.patient_id = :patientId", nativeQuery = true)
    List<MedicalHistory> searchMedicalHistory(@Param("doctorId") Long doctorId, @Param("patientId") Long patientId);

    @EntityGraph(value = "MedicalHistory.withDetails")
    Optional<MedicalHistory> findById(Long id);
}
