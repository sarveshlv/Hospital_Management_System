package com.hms.bookingms.exceptions;

public class BookingNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1013548386054732318L;

	public BookingNotFoundException(String bookingId) {
		super("Booking not found for id: " + bookingId);
	}
}