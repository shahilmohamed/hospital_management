package com.project.hospitalReport.document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "drug_log_history")
@Getter 
@Setter
public class DrugLogHistory {

    @Id
    private String id;
    private Long mysqlLogId;
    private Long stockId;
    private String drugName;
    private Long addedQuantity;
    private Long soldQuantity;
    private Long availableQuantity;
    private LocalDate updatedDate;
    private LocalTime updatedTime;
    private Long doctorId;
    private String modifiedBy;
    private String action;
}
