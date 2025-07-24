package com.project.hospitalReport.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class SignupRequest {
    private String firstname;
    private String lastname;
    @Temporal(TemporalType.DATE)
    private LocalDate dob;
    private String gender;
    private String role;
    private String specialization;
    private String email;
    private String phoneNumber;
    private String password;
}
