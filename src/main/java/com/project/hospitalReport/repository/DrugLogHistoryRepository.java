package com.project.hospitalReport.repository;

import com.project.hospitalReport.document.DrugLogHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DrugLogHistoryRepository extends MongoRepository<DrugLogHistory, String> {

    Page<DrugLogHistory> findByStockId(Long stockId, Pageable pageable);

    Optional<DrugLogHistory> findByMysqlLogId(Long mysqlLogId);
}
