package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.DrugInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugInvoiceRepo extends JpaRepository<DrugInvoice, Long> {

}
