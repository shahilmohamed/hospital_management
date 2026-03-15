package com.project.hospitalReport.dto;

import com.project.hospitalReport.entity.DrugInvoice;
import com.project.hospitalReport.entity.InvoiceItems;
import lombok.Data;

import java.util.List;

@Data
public class InvoiceRequest {
    private DrugInvoice drugInvoice;
    private List<InvoiceItems> items;
}
