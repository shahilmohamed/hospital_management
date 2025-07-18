package com.project.hospitalReport.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
public class DrugLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate updatedDate;
    private String drugName;
    private LocalTime updatedTime;
    private Long addedQuantity;
    private Long soldQuantity;
    private Long availableQuantity;
    @ManyToOne
    private DrugsStock stock;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public LocalTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public DrugsStock getStock() {
        return stock;
    }

    public void setStock(DrugsStock stock) {
        this.stock = stock;
    }

    public Long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Long getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(Long soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public Long getAddedQuantity() {
        return addedQuantity;
    }

    public void setAddedQuantity(Long addedQuantity) {
        this.addedQuantity = addedQuantity;
    }
}
