package com.project.hospitalReport.dto.drugs;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Stock {
	@Id
	private String name;
	private Long quantity;
	private Long mrp;
	private Long perPieceRate;
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
	public Long getMrp() {
		return mrp;
	}
	public void setMrp(Long mrp) {
		this.mrp = mrp;
	}
	public Long getPerPieceRate() {
		return perPieceRate;
	}
	public void setPerPieceRate(Long perPieceRate) {
		this.perPieceRate = perPieceRate;
	}
	

}
