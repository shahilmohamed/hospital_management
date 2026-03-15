package com.project.hospitalReport.service;

import com.project.hospitalReport.entity.InvoiceItems;
import com.project.hospitalReport.repository.InvoiceItemsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceItemsService {
    @Autowired
    InvoiceItemsRepo invoiceItemsRepo;

    public List<Long> insertItems(List<InvoiceItems> list)
    {
        List<InvoiceItems> hello = invoiceItemsRepo.saveAll(list);
        List<Long> ids = new ArrayList<>();
        for(InvoiceItems i : hello){
            ids.add(i.getId());
        }
        return ids;
    }
}
