package com.project.hospitalReport.entity;

import jakarta.persistence.*;

@Entity
public class Prescription {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@OneToOne
	private DrugsStock stocks;
    private Boolean dosageMorning;
    private Boolean dosageAfternoon;
    private Boolean dosageNight;
    private Integer durationDays;
    @ManyToOne
    private MedicalHistory medicalHistory;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DrugsStock getStocks() {
		return stocks;
	}

	public void setStocks(DrugsStock stocks) {
		this.stocks = stocks;
	}

	public Boolean getDosageMorning() {
		return dosageMorning;
	}

	public void setDosageMorning(Boolean dosageMorning) {
		this.dosageMorning = dosageMorning;
	}

	public Boolean getDosageAfternoon() {
		return dosageAfternoon;
	}

	public void setDosageAfternoon(Boolean dosageAfternoon) {
		this.dosageAfternoon = dosageAfternoon;
	}

	public Boolean getDosageNight() {
		return dosageNight;
	}

	public void setDosageNight(Boolean dosageNight) {
		this.dosageNight = dosageNight;
	}

	public Integer getDurationDays() {
		return durationDays;
	}

	public void setDurationDays(Integer durationDays) {
		this.durationDays = durationDays;
	}

	public MedicalHistory getMedicalHistory() {
		return medicalHistory;
	}

	public void setMedicalHistory(MedicalHistory medicalHistory) {
		this.medicalHistory = medicalHistory;
	}
    


}
