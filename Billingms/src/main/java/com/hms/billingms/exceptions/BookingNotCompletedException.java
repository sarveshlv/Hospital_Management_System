package com.hms.billingms.exceptions;

public class BookingNotCompletedException extends RuntimeException {

	private static final long serialVersionUID = -6679440189540706908L;

	public BookingNotCompletedException(String bookingStatus) {
		super("Booking not completed. Current booking staus: " + bookingStatus);
	}
}