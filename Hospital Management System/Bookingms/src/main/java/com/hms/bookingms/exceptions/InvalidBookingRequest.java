package com.hms.bookingms.exceptions;

public class InvalidBookingRequest extends RuntimeException {

	private static final long serialVersionUID = -6461827200115598604L;

	public InvalidBookingRequest(String bookingStatus) {
		super("Already approved or rejected. Current booking status: " + bookingStatus);
	}

}
