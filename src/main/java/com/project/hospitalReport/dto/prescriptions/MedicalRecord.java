package com.project.hospitalReport.dto.prescriptions;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class MedicalRecord {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Patient patient;

    private LocalDate diagnosisDate;
    private String diagnosis;
    private String notes;

    @OneToMany(mappedBy = "medicalHistory", cascade = CascadeType.ALL)
    private List<Prescription> prescriptions;
    
    private String revisitDate;
    private String review;

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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<Prescription> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(List<Prescription> prescriptions) {
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
