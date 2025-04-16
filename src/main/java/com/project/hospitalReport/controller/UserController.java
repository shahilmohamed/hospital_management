package com.project.hospitalReport.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.hospitalReport.dao.UserDao;
import com.project.hospitalReport.dto.User;
import com.project.hospitalReport.service.ApiResponse;
import com.project.hospitalReport.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class UserController {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserService service;
	
	@PostMapping("signup")
	public HashMap<String, String> signup(@RequestBody User user)
	{
		HashMap<String, String> value= userDao.signup(user);
		return value;
	}
	
	@PostMapping("login")
	public ResponseEntity<ApiResponse<User>> login(@RequestBody User user)
	{
		User value = service.login(user);
		if(value!=null)
		{
			ApiResponse<User> res = new ApiResponse<User>(value, "Login success", HttpStatus.OK.value());
			return new ResponseEntity<>(res, HttpStatus.OK);
		}
		else
		{
			ApiResponse<User> res = new ApiResponse<User>(value, "Login failed", HttpStatus.NOT_FOUND.value());
			return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
		}
	}

}
