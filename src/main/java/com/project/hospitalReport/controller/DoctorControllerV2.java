package com.project.hospitalReport.controller;

import com.project.hospitalReport.service.DoctorServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctors")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}, allowCredentials = "true")
public class DoctorControllerV2 {

    @Autowired
    DoctorServiceV2 doctorServiceV2;

}
