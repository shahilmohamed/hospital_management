package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepo extends JpaRepository<Prescription, Long> {

    List<Prescription> findByMedicalHistoryId(@Param("medicalHistoryId") Long medicalHistoryId);
}
