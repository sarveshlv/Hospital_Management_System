package com.Capg.Billing.Exception;

public class BookingNotFoundException extends RuntimeException{
    public BookingNotFoundException(String msg) {
        super(msg);
    }
}
