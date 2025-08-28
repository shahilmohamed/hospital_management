package com.project.hospitalReport.dto;

import com.project.hospitalReport.entity.Doctor;
import com.project.hospitalReport.entity.DrugsStock;
import com.project.hospitalReport.entity.Patient;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MedicalHistoryRequest {
    private LocalDate diagnosisDate;
    private String diagnosis;
    private String revisitDate;
    @Lob
    @Column(length = 6000)
    private String review;
    private Doctor doctor;
    private Patient patient;

    private List<MedicineRequest> prescriptions;

    @Data
    public static class MedicineRequest {
        private DrugsStock stock;
        private Boolean dosageMorning;
        private Boolean dosageAfternoon;
        private Boolean dosageNight;
        private Integer durationDays;
    }

    private Long appointment_id;
}
