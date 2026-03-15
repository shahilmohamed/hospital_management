package com.project.hospitalReport.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "druginvoice")
@Getter
@Setter
public class DrugInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double invoiceAmount;
    private LocalDate invoiceDate;
    private LocalTime invoiceTime;
    private String billerName;
    private String patientName;
    @OneToMany(mappedBy = "drugInvoice")
    private List<InvoiceItems> invoiceItems;
}
