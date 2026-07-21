package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.DrugInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DrugInvoiceRepo extends JpaRepository<DrugInvoice, Long> {

    Optional<DrugInvoice> findTopByOrderByIdDesc();

    Page<DrugInvoice> findAllByOrderByIdDesc(Pageable pageable);

}
