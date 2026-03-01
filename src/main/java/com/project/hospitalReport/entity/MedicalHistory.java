package com.project.hospitalReport.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medicalhistory")
@NoArgsConstructor
@Getter
@Setter
@NamedEntityGraph(
    name = "MedicalHistory.withDetails",
    attributeNodes = {
        @NamedAttributeNode(value = "prescriptions", subgraph = "prescription.stocks"),
        @NamedAttributeNode("patient"),
        @NamedAttributeNode("doctor")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "prescription.stocks",
            attributeNodes = @NamedAttributeNode("stocks")
        )
    }
)
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
