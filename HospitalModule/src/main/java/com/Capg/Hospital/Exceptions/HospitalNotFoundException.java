package com.Capg.Hospital.Exceptions;

public class HospitalNotFoundException extends RuntimeException{

    public HospitalNotFoundException(String msg) {
        super(msg);
    }
}
