package com.project.hospitalReport.helper;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.project.hospitalReport.dto.User;

public class ConfigClass {
	public static SessionFactory getSession()
	{
		System.out.println("In configclass");
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addAnnotatedClass(User.class);
		SessionFactory sf = cfg.buildSessionFactory();
		return sf;
	}

}
