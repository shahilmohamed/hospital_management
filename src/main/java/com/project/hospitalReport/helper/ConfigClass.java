package com.project.hospitalReport.helper;

import com.project.hospitalReport.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ConfigClass {
	public static SessionFactory getSession()
	{
		System.out.println("In configclass");
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addAnnotatedClass(Doctor.class);
		cfg.addAnnotatedClass(Patient.class);
		cfg.addAnnotatedClass(MedicalHistory.class);
		cfg.addAnnotatedClass(Prescription.class);
		cfg.addAnnotatedClass(DrugsStock.class);
		cfg.addAnnotatedClass(DrugLog.class);
		SessionFactory sf = cfg.buildSessionFactory();
		return sf;
	}

}
