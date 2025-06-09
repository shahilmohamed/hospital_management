package com.project.hospitalReport.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.project.hospitalReport.dto.Appointment;
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
		d.setPassword("********");
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

	public ApiResponse<List<HashMap<String, Object>>> getAllPatient(Integer id) {
		List<Object[]> list = doctorDao.getAllPatient(id);
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

	public ApiResponse<Appointment> getAppointment(Appointment appointment) {
		return null;
	}
}
