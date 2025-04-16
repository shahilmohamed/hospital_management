package com.project.hospitalReport.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.hospitalReport.dao.UserDao;
import com.project.hospitalReport.dto.User;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
public class UserController {
	
	@Autowired
	UserDao userDao;
	
	@PostMapping("signup")
	public HashMap<String, String> signup(@RequestBody User user)
	{
		HashMap<String, String> value= userDao.signup(user);
		return value;
	}

}
