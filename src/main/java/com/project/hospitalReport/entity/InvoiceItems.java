package com.project.hospitalReport.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invoiceitems")
@Getter
@Setter
public class InvoiceItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer qty;
    private Double totalPrice;
    @ManyToOne
    private DrugsStock stock;
    @ManyToOne
    private DrugInvoice drugInvoice;
}
