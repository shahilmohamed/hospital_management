package com.project.hospitalReport.repository;

import com.project.hospitalReport.entity.InvoiceItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceItemsRepo extends JpaRepository<InvoiceItems, Long> {
}
