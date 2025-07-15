package com.project.hospitalReport.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.project.hospitalReport.dto.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.hospitalReport.dao.DoctorDao;

@Service
public class DoctorService {

	@Autowired
	DoctorDao doctorDao;

	public ApiResponse<Doctor> signup(Doctor doctor) {
		String s = doctorDao.signup(doctor);
		ApiResponse<Doctor> res = new ApiResponse<>();
		if (s.equals("Account created")) {
			res.setStatus(HttpStatus.OK.value());
			res.setMessage(s);
			res.setData(doctor);
			return res;
		}
		res.setStatus(HttpStatus.FORBIDDEN.value());
		res.setMessage(s);
		res.setData(doctor);
		return res;

	}

	public ApiResponse<Doctor> login(Doctor doctor, HttpServletResponse response) {
		Doctor d = doctorDao.login(doctor);
		ApiResponse<Doctor> res = new ApiResponse<>();
		if (d != null && d.getPassword().equals(d.getPassword())) {
			d.setPassword("********");
			Cookie nameCookie = new Cookie("name", d.getFirstname()+"%20"+d.getLastname());
			nameCookie.setMaxAge(24 * 60 * 60);
			nameCookie.setPath("/");
			nameCookie.setHttpOnly(false);

			Cookie idCookie  = new Cookie("id", String.valueOf(d.getId()));
			idCookie.setMaxAge(24 * 60 * 60);
			idCookie.setPath("/");
			idCookie.setHttpOnly(false);

			response.addCookie(nameCookie);
			response.addCookie(idCookie);

			res.setStatus(HttpStatus.OK.value());
			res.setMessage("Login successfully");
			res.setData(d);
			return res;
		}
		res.setStatus(HttpStatus.FORBIDDEN.value());
		res.setMessage("Wrong credentials");
		res.setData(d);
		return res;
	}

	public ApiResponse<?> logout(HttpServletResponse response) {
		Cookie nameCookie = new Cookie("name", "");
		nameCookie.setPath("/");
		nameCookie.setMaxAge(0); // Deletes cookie
		nameCookie.setHttpOnly(false);

		Cookie idCookie = new Cookie("id", "");
		idCookie.setPath("/");
		idCookie.setMaxAge(0); // Deletes cookie
		idCookie.setHttpOnly(false);

		response.addCookie(nameCookie);
		response.addCookie(idCookie);

		return new ApiResponse<>(null, "Logged out successfully", 200);
	}
	
	public ApiResponse<Patient> addPatient(Patient p, HttpServletRequest request) {
		String msg = doctorDao.addPatient(p, request);
		ApiResponse<Patient> res = new ApiResponse<>();
		if (msg.equals("Patient added successfully")) {
			res.setStatus(HttpStatus.OK.value());
			res.setMessage(msg);
			res.setData(null);
			return res;
		} else if (msg.equals("Patient data already exist")) {
			res.setStatus(HttpStatus.OK.value());
			res.setMessage(msg);
			res.setData(null);
			return res;
		}
		res.setStatus(HttpStatus.FORBIDDEN.value());
		res.setMessage(msg);
		res.setData(null);
		return res;
	}

	public ApiResponse<List<HashMap<String, Object>>> getAllPatient(HttpServletRequest request) {
		List<Object[]> list = doctorDao.getAllPatient(request);
		ApiResponse<List<HashMap<String, Object>>> res = new ApiResponse<>();
		if(list.size()>0){
			List<HashMap<String, Object>> al = new ArrayList<>();
			for(int i = 0;i<list.size();i++)
			{
				HashMap<String, Object> hm = new HashMap<>();
				Object[] arr = list.get(i);
				for(int j=0;j<arr.length;j++)
				{
					if(j==0)
						hm.put("id", arr[j]);
					if(j==1)
						hm.put("address", arr[j]);
					if(j==2)
						hm.put("bloodGroup", arr[j]);
					if(j==3)
						hm.put("contactNumber", arr[j]);
					if(j==4)
						hm.put("firstname", arr[j]);
					if(j==5)
						hm.put("gender", arr[j]);
					if(j==6)
						hm.put("lastname", arr[j]);
					if(j==7)
						hm.put("dob", arr[j]);
				}
				al.add(hm);
			}
			res.setStatus(HttpStatus.OK.value());
			res.setMessage("Patients is fetched");
			res.setData(al);
			return res;
		}
		else{
			res.setStatus(HttpStatus.OK.value());
			res.setMessage("Can't fetch the patients");
			res.setData(null);
			return res;
		}
			
	}

	public ApiResponse<List<HashMap<String, Object>>> searchPatient(Patient patient) {
		List<Object[]> list = doctorDao.searchPatient(patient);
		ApiResponse<List<HashMap<String, Object>>> res = new ApiResponse<>();
		if(list.size()>0){
			List<HashMap<String, Object>> al = new ArrayList<>();
			for(int i = 0;i<list.size();i++)
			{
				HashMap<String, Object> hm = new HashMap<>();
				Object[] arr = list.get(i);
				for(int j=0;j<arr.length;j++)
				{
					if(j==0)
						hm.put("firstname", arr[j]);
					if(j==1)
						hm.put("lastname", arr[j]);
					if(j==2)
						hm.put("dob", arr[j]);
					if(j==3)
						hm.put("contactNumber", arr[j]);
					if(j==4)
						hm.put("patient_id", arr[j]);
				}
				al.add(hm);
			}
			res.setStatus(HttpStatus.OK.value());
			res.setMessage("Patients is fetched");
			res.setData(al);
			return res;
		}
		res.setStatus(HttpStatus.OK.value());
		res.setMessage("Can't fetch the patients");
		res.setData(null);
		return  res;
	}

	public ApiResponse<Appointment> addAppointment(Appointment appointment) {
		String msg = doctorDao.addAppointment(appointment);
		ApiResponse<Appointment> res = new ApiResponse<>();
		res.setStatus(HttpStatus.OK.value());
		res.setMessage(msg);
		res.setData(null);
		return res;
	}

	public ApiResponse<List<HashMap<String, Object>>> getAppointment(Appointment appointment) {
		List<Object[]> list = doctorDao.getAppointment(appointment);
		ApiResponse<List<HashMap<String, Object>>> res = new ApiResponse<>();
		if (list.size()>0)
		{
			List<HashMap<String, Object>> al = new ArrayList<>();
			for(int i = 0;i<list.size();i++)
			{
				HashMap<String, Object> hm = new HashMap<>();
				Object[] arr = list.get(i);
				for (int j = 0;j< arr.length;j++)
				{
					if (j==0)
						hm.put("id", arr[j]);
					else if (j==1)
						hm.put("firstname", arr[j]);
					else if (j==2)
						hm.put("lastname", arr[j]);
					else if (j==3)
						hm.put("contactNumber", arr[j]);
					else if (j==4)
						hm.put("diagnosis", arr[j]);
				}
				al.add(hm);
			}
			res.setStatus(HttpStatus.OK.value());
			res.setMessage("Appointments found");
			res.setData(al);
			return res;
		}
		else {
			res.setStatus(HttpStatus.OK.value());
			res.setMessage("No appointments found!!!");
			return res;
		}
	}

	public ApiResponse<DrugsStock> addDrug(DrugsStock drug) {
		String s = doctorDao.addDrugs(drug);
		ApiResponse<DrugsStock> res = new ApiResponse<>();
		if (s.equals("Drug added")) {
			res.setStatus(HttpStatus.OK.value());
			res.setMessage(s);
			res.setData(drug);
			return res;
		}
		res.setStatus(HttpStatus.FORBIDDEN.value());
		res.setMessage(s);
		res.setData(drug);
		return res;
	}

	public ApiResponse<DrugsStock> updateDrug(DrugsStock drug) {
		String s = doctorDao.updateDrug(drug);
		ApiResponse<DrugsStock> res = new ApiResponse<>();
		if (s.equals("Drug stock is updated.")) {
			res.setStatus(HttpStatus.OK.value());
			res.setMessage(s);
			res.setData(drug);
			return res;
		}
		res.setStatus(HttpStatus.FORBIDDEN.value());
		res.setMessage(s);
		res.setData(drug);
		return res;
	}

	public ApiResponse<List<HashMap<String, Object>>> getAllDrugs() {
		List<Object[]> list = doctorDao.getAllDrugs();
		ApiResponse<List<HashMap<String, Object>>> res = new ApiResponse<>();
		if (list.size() > 0) {
			List<HashMap<String, Object>> al = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				HashMap<String, Object> hm = new HashMap<>();
				Object[] arr = list.get(i);
				for (int j = 0; j < arr.length; j++) {
					if (j == 0)
						hm.put("id", arr[j]);
					else if (j == 1)
						hm.put("name", arr[j]);
					else if (j == 2)
						hm.put("mrp", arr[j]);
					else if (j == 3)
						hm.put("perPieceRate", arr[j]);
					else if (j == 4)
						hm.put("addedDate", arr[j]);
					else if (j == 5)
						hm.put("quantity", arr[j]);
				}
				al.add(hm);
			}
			res.setStatus(HttpStatus.OK.value());
			res.setMessage("Drugs found");
			res.setData(al);
			return res;
		} else {
			res.setStatus(HttpStatus.OK.value());
			res.setMessage("Drug list is empty!!!");
			return res;
		}
	}

	public ApiResponse<List<HashMap<String, Object>>> getDrugById(Integer id) {
		List<Object[]> list = doctorDao.getDrugById(id);
		ApiResponse<List<HashMap<String, Object>>> res = new ApiResponse<>();
		if (list.size() > 0) {
			List<HashMap<String, Object>> al = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				HashMap<String, Object> hm = new HashMap<>();
				Object[] arr = list.get(i);
				for (int j = 0; j < arr.length; j++) {
					if (j == 0)
						hm.put("id", arr[j]);
					else if (j == 1)
						hm.put("name", arr[j]);
					else if (j == 2)
						hm.put("mrp", arr[j]);
					else if (j == 3)
						hm.put("perPieceRate", arr[j]);
					else if (j == 4)
						hm.put("addedDate", arr[j]);
					else if (j == 5)
						hm.put("quantity", arr[j]);
				}
				al.add(hm);
			}
			res.setStatus(HttpStatus.OK.value());
			res.setMessage("Drug found");
			res.setData(al);
			return res;
		} else {
			res.setStatus(HttpStatus.OK.value());
			res.setMessage("No Drug found!!!");
			return res;
		}
	}

	public ApiResponse<MedicalHistory> addMedicalHistory(MedicalHistory history, HttpServletRequest request) {
		String s = doctorDao.addMedicalHistory(history, request);
		ApiResponse<MedicalHistory> res = new ApiResponse<>();
		if (s.equals("Medical history added.")) {
			res.setStatus(HttpStatus.OK.value());
			res.setMessage(s);
			res.setData(history);
			return res;
		}
		res.setStatus(HttpStatus.FORBIDDEN.value());
		res.setMessage(s);
		res.setData(history);
		return res;
	}

	public ApiResponse<Prescription> addPrescription(Prescription prescription) {
		String s = doctorDao.addPrescription(prescription);
		ApiResponse<Prescription> res = new ApiResponse<>();
		if (s.equals("Prescription added"))
		{
			res.setStatus(HttpStatus.OK.value());
			res.setMessage(s);
			res.setData(prescription);
			return res;
		}
		res.setStatus(HttpStatus.FORBIDDEN.value());
		res.setMessage(s);
		res.setData(prescription);
		return res;
	}
}
