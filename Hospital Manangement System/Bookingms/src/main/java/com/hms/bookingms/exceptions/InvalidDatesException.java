package com.hms.bookingms.exceptions;

import java.util.Date;

public class InvalidDatesException extends RuntimeException {

	private static final long serialVersionUID = -7729423867105584603L;

	public InvalidDatesException(Date bookingDate, Date occupyDate, Date releaseDate) {
		super("Release date must be before occuping date and be after current date. Booking Date : " + bookingDate
				+ ". Occupied Date : " + occupyDate + ". Release Date : " + releaseDate);

	}
}