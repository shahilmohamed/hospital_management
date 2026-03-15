package com.project.hospitalReport.service;

import com.project.hospitalReport.entity.DrugInvoice;
import com.project.hospitalReport.repository.DrugInvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DrugInvoiceService {
    @Autowired
    DrugInvoiceRepo drugInvoiceRepo;

    public DrugInvoice insertInvoice(DrugInvoice drugInvoice){
        return drugInvoiceRepo.save(drugInvoice);
    }

}
