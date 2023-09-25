package com.hms.bookingms.exceptions;

public class HospitalNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -274671549587322883L;

	public HospitalNotFoundException(String hospitalId) {
		super("Hospital not found for id: " + hospitalId);
	}
}