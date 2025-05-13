package com.project.hospitalReport.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Prescription {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@OneToOne
    private Stocks stocks;
    private String dosageMrng;
    private String dosageAf;
    private String dosageNight;
    private Integer durationDays;
    @OneToOne
    private MedicalHistory medicalHistory;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Stocks getStocks() {
		return stocks;
	}

	public void setStocks(Stocks stocks) {
		this.stocks = stocks;
	}

	public void setDurationDays(Integer durationDays) {
		this.durationDays = durationDays;
	}

	public String getDosageMrng() {
		return dosageMrng;
	}

	public void setDosageMrng(String dosageMrng) {
		this.dosageMrng = dosageMrng;
	}

	public String getDosageAf() {
		return dosageAf;
	}

	public void setDosageAf(String dosageAf) {
		this.dosageAf = dosageAf;
	}

	public String getDosageNight() {
		return dosageNight;
	}

	public void setDosageNight(String dosageNight) {
		this.dosageNight = dosageNight;
	}

	public int getDurationDays() {
		return durationDays;
	}

	public void setDurationDays(int durationDays) {
		this.durationDays = durationDays;
	}

	public MedicalHistory getMedicalHistory() {
		return medicalHistory;
	}

	public void setMedicalHistory(MedicalHistory medicalHistory) {
		this.medicalHistory = medicalHistory;
	}
    


}
