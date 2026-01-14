package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.DrugLog;
import com.project.hospitalReport.entity.DrugsStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugsLogRepo extends JpaRepository<DrugLog, Long> {
    Page<DrugLog> getByStockOrderByUpdatedDateDescUpdatedTimeDesc(DrugsStock stock, Pageable pageable);
}
