package com.project.hospitalReport.service;

import com.project.hospitalReport.repository.PrescriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionService {

    @Autowired
    PrescriptionRepo prescriptionRepo;

}
