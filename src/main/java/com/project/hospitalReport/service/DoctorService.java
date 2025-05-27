package com.project.hospitalReport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.project.hospitalReport.dao.DoctorDao;
import com.project.hospitalReport.dto.Doctor;
import com.project.hospitalReport.dto.Patient;

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

	public ApiResponse<Doctor> login(Doctor doctor) {
		Doctor d = doctorDao.login(doctor);
		ApiResponse<Doctor> res = new ApiResponse<>();
		if (d != null) {
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
	
	public ApiResponse<Patient> addPatient(Patient p, Integer id) {
		String msg = doctorDao.addPatient(p, id);
		ApiResponse<Patient> res = new ApiResponse<>();
		if (msg.equals("Patient added successfully")) {
			res.setStatus(HttpStatus.OK.value());
			res.setMessage(msg);
			res.setData(p);
			return res;
		}
		res.setStatus(HttpStatus.FORBIDDEN.value());
		res.setMessage(msg);
		res.setData(null);
		return res;
	}

}
