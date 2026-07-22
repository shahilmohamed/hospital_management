package com.project.hospitalReport.service;

import com.project.hospitalReport.dto.PageRequ;
import com.project.hospitalReport.entity.DrugInvoice;
import com.project.hospitalReport.repository.DrugInvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class DrugInvoiceService {
    @Autowired
    DrugInvoiceRepo drugInvoiceRepo;

    public DrugInvoice insertInvoice(DrugInvoice drugInvoice){
        drugInvoice.setInvoiceNumber(generateInvoiceNumber());
        return drugInvoiceRepo.save(drugInvoice);
    }

    public Page<DrugInvoice> getAllInvoice(PageRequ pageRequ){
        Pageable pageable = PageRequest.of(pageRequ.getPage(), pageRequ.getSize());
        return drugInvoiceRepo.findAllByOrderByIdDesc(pageable);
    }

    public String generateInvoiceNumber(){
        String invoiceNumber = "";
        Optional<DrugInvoice> lastInvoice = drugInvoiceRepo.findTopByOrderByIdDesc();
        LocalDate today = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyy");
        String prefix = format.format(today);
        Integer nextSequence = 1;
        if (lastInvoice.isPresent()){
            String lastInvoiceNumber = lastInvoice.get().getInvoiceNumber();
            if (lastInvoiceNumber != null && lastInvoiceNumber.startsWith(prefix)){
                String currentSequence = lastInvoiceNumber.substring(prefix.length());
                try {
                    nextSequence = Integer.parseInt(currentSequence) + 1;
                } catch (NumberFormatException e) {
                    nextSequence = 1;
                }
            }

        }
        invoiceNumber = prefix + "IN" + String.format("%02d", nextSequence);
        return invoiceNumber;
    }

}
