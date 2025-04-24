package com.project.hospitalReport.helper;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.project.hospitalReport.dto.Doctor;
import com.project.hospitalReport.dto.DrugsStock;
import com.project.hospitalReport.dto.MedicalHistory;
import com.project.hospitalReport.dto.Patient;
import com.project.hospitalReport.dto.Prescription;
import com.project.hospitalReport.dto.StockAdd;
import com.project.hospitalReport.dto.Stocks;
import com.project.hospitalReport.dto.User;

public class ConfigClass {
	public static SessionFactory getSession()
	{
		System.out.println("In configclass");
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");
		cfg.addAnnotatedClass(User.class);
		cfg.addAnnotatedClass(Doctor.class);
		cfg.addAnnotatedClass(Patient.class);
		cfg.addAnnotatedClass(MedicalHistory.class);
		cfg.addAnnotatedClass(Prescription.class);
		cfg.addAnnotatedClass(Stocks.class);
		cfg.addAnnotatedClass(DrugsStock.class);
		cfg.addAnnotatedClass(StockAdd.class);
		SessionFactory sf = cfg.buildSessionFactory();
		return sf;
	}

}
