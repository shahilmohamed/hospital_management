package com.project.hospitalReport.dto;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class DrugsStock {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private Long quantity;
	private Double mrp;
	private Double perPieceRate;
	private LocalDate addedDate;
	private LocalDate updatedDate;
	@OneToMany(mappedBy = "stock")
	private List<DrugLog> drugLogs;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public Double getMrp() {
		return mrp;
	}
	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}
	public Double getPerPieceRate() {
		return perPieceRate;
	}
	public void setPerPieceRate(Double perPieceRate) {
		this.perPieceRate = perPieceRate;
	}

	public LocalDate getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(LocalDate addedDate) {
		this.addedDate = addedDate;
	}

	public LocalDate getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDate updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<DrugLog> getDrugLogs() {
		return drugLogs;
	}

	public void setDrugLogs(List<DrugLog> drugLogs) {
		this.drugLogs = drugLogs;
	}
}
