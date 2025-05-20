package com.project.hospitalReport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.hospitalReport.dao.DoctorDao;
import com.project.hospitalReport.dto.Doctor;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class DoctorController {
	
	@Autowired
	DoctorDao dao;
	
	@GetMapping("hello")
	public void hello()
	{
		ResponseEntity<String> res = new ResponseEntity<>(HttpStatus.OK);
		System.out.println(res);
		
	}
	
	@PostMapping("signup")
	public String signup(@RequestBody Doctor doctor)
	{
		String result = dao.signup(doctor);
		return result;
	}
	

}
