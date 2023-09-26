package com.Capg.Booking.Exception;

public class InvalidBookingRequest extends RuntimeException{
    public InvalidBookingRequest(String msg) {
        super(msg);
    }
}
