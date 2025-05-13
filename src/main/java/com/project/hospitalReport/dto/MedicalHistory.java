package com.project.hospitalReport.dto;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class MedicalHistory {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate diagnosisDate;
    private String diagnosis;
    private String revisitDate;
    private String review;
    @OneToOne
    private Prescription prescriptions;
    @ManyToOne
    private Patient patient;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public LocalDate getDiagnosisDate() {
		return diagnosisDate;
	}

	public void setDiagnosisDate(LocalDate diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	
	public Prescription getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(Prescription prescriptions) {
		this.prescriptions = prescriptions;
	}

	public String getRevisitDate() {
		return revisitDate;
	}

	public void setRevisitDate(String revisitDate) {
		this.revisitDate = revisitDate;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}
    
    
	

}
