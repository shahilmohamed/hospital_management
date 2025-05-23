package com.project.hospitalReport.dao;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.project.hospitalReport.dto.Doctor;
import com.project.hospitalReport.helper.ConfigClass;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class DoctorDao {

	public String signup(Doctor doctor) {
		// TODO Auto-generated method stub
		Doctor d = new Doctor();
		Date date = new Date();
		d.setFirstname(doctor.getFirstname());
		d.setLastname(doctor.getLastname());
		d.setDob(doctor.getDob());
		d.setGender(doctor.getGender());
		d.setRole(doctor.getRole());
		d.setSpecialization(doctor.getSpecialization());
		d.setEmail(doctor.getEmail());
		d.setPhoneNumber(doctor.getPhoneNumber());
		d.setPassword(doctor.getPassword());
		Session session = ConfigClass.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Doctor> query = builder.createQuery(Doctor.class);
		Root<Doctor> root = query.from(Doctor.class);
		Predicate condition = builder.equal(root.get("email"), d.getEmail());
		query.select(root).where(condition);
		Doctor result = session.createQuery(query).getSingleResultOrNull();
		if (result == null) {
			session.save(d);
			transaction.commit();
			session.close();
			return "Account created";
		}
		session.close();
		return "User present";

	}

	public Doctor login(Doctor doctor) {
		Session session = ConfigClass.getSession().openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Doctor> query = builder.createQuery(Doctor.class);
		Root<Doctor> root = query.from(Doctor.class);
		Predicate condition1 = builder.equal(root.get("email"), doctor.getEmail());
		Predicate condition2 = builder.equal(root.get("password"), doctor.getPassword());
		query.select(root).where(builder.and(condition1, condition2));
		Doctor result = session.createQuery(query).getSingleResultOrNull();
		session.close();
		return result;

	}

}
