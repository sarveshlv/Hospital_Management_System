package com.hms.billingms.exceptions;

public class BookingNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8491516864786185399L;

	public BookingNotFoundException(String bookingId) {
		super("Booking not found for id: " + bookingId);
	}
}