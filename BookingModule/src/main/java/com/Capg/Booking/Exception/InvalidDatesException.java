package com.Capg.Booking.Exception;

import java.util.Date;

public class InvalidDatesException extends RuntimeException{

    public InvalidDatesException(Date bookingDate, Date occupyDate, Date releaseDate) {
        super("Release date must be before occuping date and be after current date" + "\nBooking Date : " + bookingDate
                + "\nOccupied Date : " + occupyDate + "\nRelease Date : " + releaseDate);

    }
}
