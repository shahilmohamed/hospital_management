package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.DrugLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugsLogRepo extends JpaRepository<DrugLog, Long> {
}
