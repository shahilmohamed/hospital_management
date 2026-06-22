package com.project.hospitalReport.service;

import com.project.hospitalReport.dto.PageRequ;
import com.project.hospitalReport.entity.DrugInvoice;
import com.project.hospitalReport.repository.DrugInvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DrugInvoiceService {
    @Autowired
    DrugInvoiceRepo drugInvoiceRepo;

    public DrugInvoice insertInvoice(DrugInvoice drugInvoice){
        return drugInvoiceRepo.save(drugInvoice);
    }

    public Page<DrugInvoice> getAllInvoice(PageRequ pageRequ){
        Pageable pageable = PageRequest.of(pageRequ.getPage(), pageRequ.getSize());
        return drugInvoiceRepo.findAll(pageable);
    }

}
