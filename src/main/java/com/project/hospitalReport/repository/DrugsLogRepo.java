package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.DrugLog;
import com.project.hospitalReport.entity.DrugsStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface DrugsLogRepo extends JpaRepository<DrugLog, Long> {
    Page<DrugLog> findByStock(DrugsStock stock, Pageable pageable);

    Page<DrugLog> findByStockAndUpdatedDateBetween(DrugsStock stock, LocalDate fromDate, LocalDate toDate, Pageable pageable);
}
