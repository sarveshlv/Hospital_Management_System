package com.hms.patientms.service;

import com.hms.patientms.dto.AddPatientRequest;
import com.hms.patientms.entities.Patient;
import com.hms.patientms.exceptions.PatientNotFoundException;

public interface IPatientService {
	Patient addPatient(AddPatientRequest addPatientRequest);
	Patient updatePatient(String id, AddPatientRequest addPatientRequest) throws PatientNotFoundException; 
	Patient findPatientById(String id) throws PatientNotFoundException;
}
