package com.project.hospitalReport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.project.hospitalReport.entity")
public class HospitalReportApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(HospitalReportApplication.class, args);
		System.out.println("Application started");
		
	}

}
