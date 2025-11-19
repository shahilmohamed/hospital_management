package com.project.hospitalReport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.project.hospitalReport.entity", "com.security.jwt"})
public class HospitalReportApplication {
	public static void main(String[] args) {
		SpringApplication.run(HospitalReportApplication.class, args);
		System.out.println("Application started!");
//		var key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//		String base64Key = Encoders.BASE64.encode(key.getEncoded());
//		System.out.println("JWT Secret Key: " + base64Key);
//
	}

}
