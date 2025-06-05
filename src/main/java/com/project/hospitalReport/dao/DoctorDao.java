package com.project.hospitalReport.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.project.hospitalReport.dto.Appointment;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import com.project.hospitalReport.dto.Doctor;
import com.project.hospitalReport.dto.Patient;
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
	
	public String addPatient(Patient p, Integer id) {
		Session session = ConfigClass.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		String sql = "select * " +
				"from hospital.patient p " +
				"where (p.firstname = :fname or p.lastname = :lname) and (p.contactNumber = :contact);";
		NativeQuery<Object[]> query = session.createNativeQuery(sql);
		query.setParameter("fname", p.getFirstname());
		query.setParameter("lname", p.getLastname());
		query.setParameter("contact", p.getContactNumber());
		List<Object[]> result = query.getResultList();
		try {
			Doctor doctor = session.get(Doctor.class, id);
			if (doctor == null) {
				System.out.println("Doctor not found!");
				session.close();
				return "Doctor not found!";
			}
			if (result.size()==0)
			{
				Patient patient = new Patient();
				patient.setFirstname(p.getFirstname());
				patient.setLastname(p.getLastname());
				patient.setGender(p.getGender());
				patient.setBloodGroup(p.getBloodGroup());
				patient.setDob(p.getDob());
				patient.setContactNumber(p.getContactNumber());
				patient.setAddress(p.getAddress());
				patient.getDoctors().add(doctor);
				doctor.getPatients().add(patient);
				session.persist(patient);
				transaction.commit();
			}
			else {
				return "Patient data already exist";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Error while adding patinen";
		}
		session.close();
		return "Patient added successfully";
	}
	
	public List<Object[]> getAllPatient(Integer id)
	{
		Session session = ConfigClass.getSession().openSession();
		session.beginTransaction();
		String sql = "SELECT m_1.id, m_1.address, m_1.bloodGroup, m_1.contactNumber, m_1.firstname, m_1.gender, m_1.lastname, m_1.dob "
				+ "FROM patient_doctor m_0 "
				+ "JOIN patient m_1 ON m_1.id = m_0.patient_id "
				+ "WHERE m_0.doctor_id= :doctor_id";
		NativeQuery<Object[]> query = session.createNativeQuery(sql);
		query.setParameter("doctor_id", id);
		List<Object[]> result = query.getResultList();
		session.close();
		
		return result;
	}

	public List<Object[]> searchPatient(Patient patient) {
		Session session = ConfigClass.getSession().openSession();
		session.beginTransaction();
		String sql = "SELECT p.firstname, p.lastname, p.dob, p.contactNumber " +
				"FROM patient p " +
				"WHERE p.contactNumber = :contactNumber OR p.id = :id ;";
		NativeQuery<Object[]> query = session.createNativeQuery(sql);
		query.setParameter("contactNumber", patient.getContactNumber());
		query.setParameter("id", patient.getId());
		List<Object[]> result = query.getResultList();
		session.close();
		return result;
	}

	public String addAppointment(Appointment appointment) {
		Appointment a = new Appointment();
		a.setFirstname(appointment.getFirstname());
		a.setLastname(appointment.getLastname());
		a.setContactNumber(appointment.getContactNumber());
		a.setDiagnosis(appointment.getDiagnosis());
		a.setDiagnosisDate(appointment.getDiagnosisDate());
		Session session = ConfigClass.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		session.save(a);
		transaction.commit();
		session.close();
		return "Appointment added";
	}
}
