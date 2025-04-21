package com.project.hospitalReport.dto;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class QtyAddDate {
	@Id
	private Integer id;
	private LocalDate date;
	private Stocks stocks;
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
	public Stocks getStock() {
		return stocks;
	}
	public void setStock(Stocks stocks) {
		this.stocks = stocks;
	}
	public Long getQty() {
		return qty;
	}
	public void setQty(Long qty) {
		this.qty = qty;
	}
	

}
