package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepo extends JpaRepository<Prescription, Long> {

    @Query(value = "select p.id, p.dosageMorning, p.dosageAfternoon, p.dosageNight, p.durationDays, p.medicalHistory_id, p.stocks_id " +
            "from prescription as p " +
            "where p.medicalHistory_id = :medicalHistoryId", nativeQuery = true)
    List<Prescription> getPrescription(@Param("medicalHistoryId") Long medicalHistoryId);
}
