package com.hms.bookingms.exceptions;

public class PatientNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -2366058171749724895L;

	public PatientNotFoundException(String patientId) {
		super("Patient not found for id: " + patientId);
	}

}
