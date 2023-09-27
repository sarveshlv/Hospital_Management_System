package com.hms.userms.exception;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1797847212004039775L;

	public UserNotFoundException(String email) {
		super("User not found for email Id: " + email);
	}
}