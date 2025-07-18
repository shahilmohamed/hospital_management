package com.project.hospitalReport.controller;

import java.util.HashMap;
import java.util.List;

import com.project.hospitalReport.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.hospitalReport.service.ApiResponse;
import com.project.hospitalReport.service.DoctorService;

@RestController
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,
		RequestMethod.DELETE}, allowCredentials = "true")
public class DoctorController {

	@Autowired
	DoctorService doctorService;

	@GetMapping("hello")
	public void hello() {
		ResponseEntity<String> res = new ResponseEntity<>(HttpStatus.OK);
		System.out.println(res);

	}

	@PostMapping("signup")
	public ApiResponse<Doctor> signup(@RequestBody Doctor doctor) {
		ApiResponse<Doctor> result = doctorService.signup(doctor);
		return result;
	}

	@PostMapping("login")
	public ApiResponse<Doctor> login(@RequestBody Doctor doctor, HttpServletResponse response) {
		ApiResponse<Doctor> result = doctorService.login(doctor, response);
		return result;
	}

	@GetMapping("/logout")
	public ApiResponse<?> logout(HttpServletResponse response) {
		ApiResponse<?> result = doctorService.logout(response);
		return result;
	}

	@PostMapping("addPatient")
	public ApiResponse<Patient> addPatient(@RequestBody Patient p, HttpServletRequest request) {
		ApiResponse<Patient> result = doctorService.addPatient(p, request);
		return result;
	}

	@GetMapping("getAllPatient")
	public ApiResponse<List<HashMap<String, Object>>> getAllPatient(HttpServletRequest request) {
		ApiResponse<List<HashMap<String, Object>>> result = doctorService.getAllPatient(request);
		return result;
	}

	@PostMapping("searchPatient")
	public ApiResponse<List<HashMap<String, Object>>> searchPatient(@RequestBody Patient patient, HttpServletRequest request)
	{
		ApiResponse<List<HashMap<String, Object>>> result = doctorService.searchPatient(patient, request);
		return result;
	}

	@PostMapping("addAppointment")
	public ApiResponse<Appointment> addAppointment(@RequestBody Appointment appointment)
	{
		ApiResponse<Appointment> result = doctorService.addAppointment(appointment);
		return result;
	}

	@PostMapping("getAppointment")
	public ApiResponse<List<HashMap<String, Object>>> getAppointment(@RequestBody Appointment appointment)
	{
		ApiResponse<List<HashMap<String, Object>>> result = doctorService.getAppointment(appointment);
		return result;
	}

	@PostMapping("addDrug")
	public ApiResponse<DrugsStock> addDrug(@RequestBody DrugsStock drug) {
		ApiResponse<DrugsStock> result = doctorService.addDrug(drug);
		return result;
	}

	@PutMapping("updateDrug")
	public ApiResponse<DrugsStock> updateDrug(@RequestBody DrugsStock drug) {
		ApiResponse<DrugsStock> result = doctorService.updateDrug(drug);
		return result;
	}

	@GetMapping("getAllDrugs")
	public ApiResponse<List<HashMap<String, Object>>> getAllDrugs() {
		ApiResponse<List<HashMap<String, Object>>> result = doctorService.getAllDrugs();
		return result;
	}

	@GetMapping("getDrugById")
	public ApiResponse<List<HashMap<String, Object>>> getAllDrugs(@RequestBody DrugsStock d) {
		Integer id = d.getId();
		ApiResponse<List<HashMap<String, Object>>> result = doctorService.getDrugById(id);
		return result;
	}


	@PostMapping("addMedicalHistory")
	public ApiResponse<MedicalHistory> addMedicalHistory(@RequestBody MedicalHistory history, HttpServletRequest request) {
		ApiResponse<MedicalHistory> result = doctorService.addMedicalHistory(history, request);
		return result;
	}

	@PostMapping("addPrescription")
	public ApiResponse<Prescription> addPrescription(@RequestBody Prescription prescription) {
		ApiResponse<Prescription> result = doctorService.addPrescription(prescription);
		return result;
	}

	@PostMapping("getPrescription")
	public ApiResponse<List<HashMap<String, Object>>> getPrescription(@RequestBody MedicalHistory medicalHistory)
	{
		ApiResponse<List<HashMap<String, Object>>> result = doctorService.getPrescription(medicalHistory);
		return result;
	}

	@PostMapping("getMedicalHistory")
	public ApiResponse<List<HashMap<String, Object>>> getMedicalHistory(@RequestBody Patient patient)
	{
		ApiResponse<List<HashMap<String, Object>>> result = doctorService.getMedicalHistory(patient);
		return result;
	}

}
