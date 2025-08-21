package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepo extends JpaRepository<MedicalHistory, Long> {
}
