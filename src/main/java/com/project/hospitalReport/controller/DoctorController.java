package com.project.hospitalReport.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.hospitalReport.dto.Doctor;
import com.project.hospitalReport.dto.Patient;
import com.project.hospitalReport.service.ApiResponse;
import com.project.hospitalReport.service.DoctorService;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,
		RequestMethod.DELETE })
public class DoctorController {

	@Autowired
	DoctorService doctorService;

	@GetMapping("hello")
	public void hello() {
		ResponseEntity<String> res = new ResponseEntity<>(HttpStatus.OK);
		System.out.println(res);

	}

	@PostMapping("signup")
	public ApiResponse<Doctor> signup(@RequestBody Doctor doctor) {
		ApiResponse<Doctor> result = doctorService.signup(doctor);
		return result;
	}

	@PostMapping("login")
	public ApiResponse<Doctor> login(@RequestBody Doctor doctor) {
		ApiResponse<Doctor> result = doctorService.login(doctor);
		return result;
	}
	
	@PostMapping("addPatient/{id}")
	public ApiResponse<Patient> addPatient(@RequestBody Patient p, @PathVariable String id) {
		Integer idm = Integer.parseInt(id);
		ApiResponse<Patient> result = doctorService.addPatient(p, idm);
		return result;
	}
	
	@GetMapping("getAllPatient/{id}")
	public ApiResponse<List<HashMap<String, Object>>> getAllPatient(@PathVariable String id)
	{
		Integer idm = Integer.parseInt(id);
		ApiResponse<List<HashMap<String, Object>>> result = doctorService.getAllPatient(idm);
		return result;
	}

}
