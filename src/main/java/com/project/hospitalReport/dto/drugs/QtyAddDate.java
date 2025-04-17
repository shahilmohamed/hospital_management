package com.project.hospitalReport.dto.drugs;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class QtyAddDate {
	@Id
	private Integer id;
	private LocalDate date;
	private Stock stock;
	private Long qty;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public Long getQty() {
		return qty;
	}
	public void setQty(Long qty) {
		this.qty = qty;
	}
	

}
