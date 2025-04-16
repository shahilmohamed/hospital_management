package com.project.hospitalReport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.hospitalReport.dao.UserDao;
import com.project.hospitalReport.dto.User;

@Service
public class UserService {
	
	@Autowired
	UserDao userDao;
	
	public User login(User user)
	{
		return userDao.login(user);
		
	}

}
