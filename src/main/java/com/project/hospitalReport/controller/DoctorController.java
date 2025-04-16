package com.project.hospitalReport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class DoctorController {
	
	@GetMapping("hello")
	public void hello()
	{
		ResponseEntity<String> res = new ResponseEntity<>(HttpStatus.OK);
		System.out.println(res);
		
	}
	

}
