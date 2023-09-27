package com.hms.patientms.exceptions;

public class PatientNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4183934707477773097L;

	public PatientNotFoundException(String id) {
		super("Patient not found for : " + id);
	}

}
