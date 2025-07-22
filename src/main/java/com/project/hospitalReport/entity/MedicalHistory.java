package com.project.hospitalReport.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class MedicalHistory {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate diagnosisDate;
    private String diagnosis;
    private String revisitDate;
	@Lob
	@Column(length = 6000)
    private String review;
    @OneToMany(mappedBy = "medicalHistory")
    private List<Prescription> prescriptions;
    @ManyToOne
    private Patient patient;
	@ManyToOne
	private Doctor doctor;
	

}
