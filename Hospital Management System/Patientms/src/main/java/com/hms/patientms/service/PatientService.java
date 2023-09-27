package com.hms.patientms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.patientms.dto.AddPatientRequest;
import com.hms.patientms.entities.Patient;
import com.hms.patientms.exceptions.PatientNotFoundException;
import com.hms.patientms.repository.PatientRepository;

@Service
public class PatientService implements IPatientService {

	@Autowired
	private PatientRepository patientRepository;


	@Override
	public Patient addPatient(AddPatientRequest addPatientRequest) {
		Patient patient = new Patient();
		patient.setFirstName(addPatientRequest.getFirstName());
		patient.setLastName(addPatientRequest.getLastName());
		patient.setContactNumber(addPatientRequest.getContactNumber());
		patient.setAadharCard(addPatientRequest.getAadharNumber());
		patient.setAddress(addPatientRequest.getAddress());

		patient = patientRepository.save(patient);
		return patient;
	}

	@Override
	public Patient updatePatient(String id, AddPatientRequest addPatientRequest) throws PatientNotFoundException {
		Patient patient = findPatientById(id);
		patient.setFirstName(addPatientRequest.getFirstName());
		patient.setLastName(addPatientRequest.getLastName());
		patient.setContactNumber(addPatientRequest.getContactNumber());
		patient.setAadharCard(addPatientRequest.getAadharNumber());
		patient.setAddress(addPatientRequest.getAddress());
		return patientRepository.save(patient);
	}

	@Override
	public Patient findPatientById(String id) throws PatientNotFoundException {
		return patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(id));
	}

}
