package com.hms.hospitalms.exception;

public class HospitalNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -5039752930885494280L;

	public HospitalNotFoundException(String id) {
		super("Hospital not found for : " + id);
	}
}