package com.project.hospitalReport.dao;

import java.time.LocalTime;
import java.util.List;

import com.project.hospitalReport.dto.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

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
	
	public String addPatient(Patient p, HttpServletRequest request) {
		String userId = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("id".equals(cookie.getName())) {
					userId = cookie.getValue();
				}
			}
		}
		Integer doctor_id = Integer.parseInt(userId);
		Session session = ConfigClass.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		String sql = "select p.id, p.firstname, p.lastname, p.gender, p.contactNumber, p.address, p.bloodGroup, p.dob " +
				"from hospital.patient p " +
				"where (p.firstname = :fname or p.lastname = :lname) and (p.contactNumber = :contact);";
		NativeQuery<Object[]> query = session.createNativeQuery(sql);
		query.setParameter("fname", p.getFirstname());
		query.setParameter("lname", p.getLastname());
		query.setParameter("contact", p.getContactNumber());
		List<Object[]> result = query.getResultList();
		try {
			Doctor doctor = session.get(Doctor.class, doctor_id);
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
			else if (result.size()==1){
				List<Object[]> list = result;
				Integer pid=0;
				for (int i = 0;i< list.size();i++)
				{
					Object[] arr = list.get(i);
					pid = Integer.parseInt(arr[i].toString());
				}
				String sql1 = "select pd.patient_id, pd.doctor_id " +
						"from patient_doctor pd " +
						"where pd.patient_id = :pid and pd.doctor_id = :did";
				NativeQuery<Object> query1 = session.createNativeQuery(sql1);
				query1.setParameter("pid", pid);
				query1.setParameter("did",doctor_id);
				List<Object> result1 = query1.getResultList();
				if (result1.size()>0)
					return "Patient data already exist";
				else
				{
					Patient patient1 = session.get(Patient.class, pid);
					patient1.getDoctors().add(doctor);
					doctor.getPatients().add(patient1);
					session.persist(patient1);
					transaction.commit();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Error while adding patinen";
		}
		session.close();
		return "Patient added successfully";
	}
	
	public List<Object[]> getAllPatient(HttpServletRequest request)
	{
		String userId = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("id".equals(cookie.getName())) {
					userId = cookie.getValue();
				}
			}
		}
		Integer doctor_id = Integer.parseInt(userId);
		Session session = ConfigClass.getSession().openSession();
		session.beginTransaction();
		String sql = "SELECT p.id, p.address, p.bloodGroup, p.contactNumber, p.firstname, p.gender, p.lastname, p.dob "
				+ "FROM patient_doctor pd "
				+ "JOIN patient p ON p.id = pd.patient_id "
				+ "WHERE pd.doctor_id= :doctor_id";
		NativeQuery<Object[]> query = session.createNativeQuery(sql);
		query.setParameter("doctor_id", doctor_id);
		List<Object[]> result = query.getResultList();
		session.close();
		
		return result;
	}

	public List<Object[]> searchPatient(Patient patient, HttpServletRequest request) {
		String userId = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("id".equals(cookie.getName())) {
					userId = cookie.getValue();
				}
			}
		}
		Session session = ConfigClass.getSession().openSession();
		session.beginTransaction();
		String sql = "SELECT p.firstname, p.lastname, p.dob, p.contactNumber, p.id, pd.`doctor_id` " +
				"FROM patient p " +
				"LEFT JOIN patient_doctor pd ON p.id = pd.`patient_id` " +
				"WHERE (p.`contactNumber` = :contactNumber OR p.`id` = :id) AND pd.`doctor_id`= :userId;";
		NativeQuery<Object[]> query = session.createNativeQuery(sql);
		query.setParameter("contactNumber", patient.getContactNumber());
		query.setParameter("id", patient.getId());
		query.setParameter("userId", userId);
		List<Object[]> result = query.getResultList();
		session.close();
		return result;
	}

	public String addAppointment(Appointment a) {
		Appointment appointment = new Appointment();
		Integer pid = a.getId();
		Session session = ConfigClass.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		try{
			Patient patient = session.get(Patient.class,pid);
			if (patient==null)
				return "No patient found";
			appointment.setFirstname(a.getFirstname());
			appointment.setLastname(a.getLastname());
			appointment.setContactNumber(a.getContactNumber());
			appointment.setDiagnosis(a.getDiagnosis());
			appointment.setDiagnosisDate(a.getDiagnosisDate());
			appointment.setIsConsulted(a.getIsConsulted());
			appointment.setPatient(patient);
			session.save(appointment);
			transaction.commit();
			session.close();
			return "Appointment added";
		} catch (Exception e) {
			return "Exception : "+e;
		}

	}

	public List<Object[]> getAppointment(Appointment appointment) {
		Session session = ConfigClass.getSession().openSession();
		session.beginTransaction();
		String localDate = appointment.getDiagnosisDate().toString();
		String sql = "SELECT a.id, a.firstname, a.lastname, a.contactNumber, a.diagnosis, a.patient_id " +
				"FROM appointment a " +
				"WHERE a.diagnosisDate = :localDate " +
				"AND a.isConsulted = 0";
		NativeQuery<Object[]> query = session.createNativeQuery(sql);
		query.setParameter("localDate", localDate);
		List<Object[]> result = query.getResultList();
		session.close();
		return result;
	}

	public String addDrugs(DrugsStock drug) {
		DrugsStock d = new DrugsStock();
		DrugLog dl = new DrugLog();

		d.setName(drug.getName());
		d.setMrp(drug.getMrp());
		d.setQuantity(drug.getQuantity());
		d.setPerPieceRate(drug.getPerPieceRate());
		d.setAddedDate(drug.getAddedDate());
		d.setUpdatedDate(drug.getUpdatedDate());

		dl.setDrugName(drug.getName());
		dl.setAddedQuantity(drug.getQuantity());
		dl.setAvailableQuantity(drug.getQuantity());
		dl.setUpdatedDate(drug.getUpdatedDate());
		dl.setUpdatedTime(LocalTime.now());

		Session session = ConfigClass.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<DrugsStock> query = builder.createQuery(DrugsStock.class);
		Root<DrugsStock> root = query.from(DrugsStock.class);
		Predicate condition = builder.equal(root.get("name"), d.getName());
		query.select(root).where(condition);
		DrugsStock result = session.createQuery(query).getSingleResultOrNull();
		if (result == null) {
			session.save(d);
			dl.setStock(d);
			session.save(dl);
			transaction.commit();
			session.close();
			return "Drug added";
		}
		session.close();
		return "Drug already present";
	}

	public String updateDrug(DrugsStock updated) {
		Integer id = updated.getId();
		Session session = ConfigClass.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		DrugsStock actual = session.get(DrugsStock.class, id);
		if (actual != null) {
			DrugLog dl = new DrugLog();

			Long check = updated.getQuantity() - actual.getQuantity();
			actual.setUpdatedDate(updated.getUpdatedDate());
			actual.setQuantity(updated.getQuantity());
			actual.setMrp(updated.getMrp());
			actual.setPerPieceRate(updated.getPerPieceRate());

			if (check > 0) {
				dl.setAddedQuantity(check);
				dl.setSoldQuantity(0L);
			} else {
				Long positive = (check < 0) ? -check : check;
				dl.setSoldQuantity(positive);
				dl.setAddedQuantity(0L);
			}
			dl.setAvailableQuantity(updated.getQuantity());
			dl.setDrugName(updated.getName());
			dl.setAvailableQuantity(updated.getQuantity());
			dl.setUpdatedDate(updated.getUpdatedDate());
			dl.setUpdatedTime(LocalTime.now());
			session.update(actual);
			transaction.commit();
			dl.setStock(actual);
			session.save(dl);
			session.close();
			return "Drug stock is updated.";
		}
		session.close();
		return "Can't fetch the drug";
	}

	public List<Object[]> getAllDrugs(){
		Session session = ConfigClass.getSession().openSession();
		session.beginTransaction();
		String sql = "SELECT ds.id, ds.name, ds.mrp, ds.perPieceRate, ds.addedDate, ds.quantity " +
				"FROM drugsstock ds";
		NativeQuery<Object[]> query = session.createNativeQuery(sql);
		List<Object[]> result = query.getResultList();
		session.close();
		return result;
	}

	public List<Object[]> getDrugById(Integer id){
		Session session = ConfigClass.getSession().openSession();
		session.beginTransaction();
		String sql = "SELECT ds.id, ds.name, ds.mrp, ds.perPieceRate, ds.addedDate, ds.quantity " +
				"FROM drugsstock ds " +
				"WHERE ds.id = :id";
		NativeQuery<Object[]> query = session.createNativeQuery(sql);
		query.setParameter("id", id);
		List<Object[]> result = query.getResultList();
		session.close();
		return result;
	}

	public String addMedicalHistory(MedicalHistory history, HttpServletRequest request) {
		String userId = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("id".equals(cookie.getName())) {
					userId = cookie.getValue();
				}
			}
		}
		Integer doctor_id = Integer.parseInt(userId);
		Session session = ConfigClass.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		try {
			Patient patient = session.get(Patient.class, history.getId());
			Doctor doctor = session.get(Doctor.class, doctor_id);
			if (patient != null && doctor != null) {
				MedicalHistory medicalHistory = new MedicalHistory();
				medicalHistory.setDiagnosis(history.getDiagnosis());
				medicalHistory.setDiagnosisDate(history.getDiagnosisDate());
				medicalHistory.setReview(history.getReview());
				medicalHistory.setRevisitDate(history.getRevisitDate());
				medicalHistory.setPatient(patient);
				medicalHistory.setDoctor(doctor);

				session.save(medicalHistory);
				transaction.commit();
				session.close();
				return "Medical history added.";
			}
		} catch (Exception e) {
			session.close();
			return "Exception: " + e;
		}
		session.close();
		return "No patient found";
	}

	public String addPrescription(Prescription prescription) {
		Session session = ConfigClass.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		MedicalHistory history = session.get(MedicalHistory.class, prescription.getMedicalHistory().getId());
		DrugsStock stock = session.get(DrugsStock.class, prescription.getStocks().getId());
		if (history != null && stock != null)
		{
			Prescription p = new Prescription();
			p.setMedicalHistory(prescription.getMedicalHistory());
			p.setStocks(prescription.getStocks());
			p.setDurationDays(prescription.getDurationDays());
			p.setDosageAfternoon(prescription.getDosageAfternoon());
			p.setDosageMorning(prescription.getDosageMorning());
			p.setDosageNight(prescription.getDosageNight());
			session.save(p);
			transaction.commit();
			session.close();
			return "Prescription added";
		}
		session.close();
		return "Can't add prescription";
	}

	public List<Object[]> getPrescription(MedicalHistory history) {
		Session session = ConfigClass.getSession().openSession();
		session.beginTransaction();
		String sql = "SELECT ds.name, ds.mrp, ds.perPieceRate, p.dosageMorning, p.dosageAfternoon, p.dosageNight,p.durationDays, m.diagnosisDate " +
				"FROM prescription AS p " +
				"LEFT JOIN drugsstock AS ds ON ds.id = p.stocks_id " +
				"LEFT JOIN medicalhistory AS m ON m.id = p.medicalHistory_id " +
				"WHERE m.id = :historyId;";
		NativeQuery<Object[]> query = session.createNativeQuery(sql);
		query.setParameter("historyId", history.getId());
		List<Object[]> result = query.getResultList();
		session.close();
		return result;
	}

	public List<Object[]> getMedicalHistory(Patient patient) {
		Session session = ConfigClass.getSession().openSession();
		session.beginTransaction();
		String sql = "SELECT mh.`id`, mh.`diagnosis`, mh.`diagnosisDate`, mh.`review`, mh.`revisitDate`, CONCAT(p.`firstname`,' ' , p.`lastname`) AS 'Patient Name', CONCAT(d.`firstname`,' ', d.`lastname`) AS 'Doctor Name' " +
				"FROM medicalhistory AS mh " +
				"LEFT JOIN patient AS p ON p.`id` = mh.`patient_id` " +
				"LEFT JOIN doctor AS d ON d.`id` = mh.`doctor_id` " +
				"WHERE p.`id`= :patient_id;";
		NativeQuery<Object[]> query = session.createNativeQuery(sql);
		query.setParameter("patient_id", patient.getId());
		List<Object[]> result = query.getResultList();
		session.close();
		return result;
	}
}
