package com.project.hospitalReport.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Stocks {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Long quantity;
	private Long mrp;
	private Long perPieceRate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
