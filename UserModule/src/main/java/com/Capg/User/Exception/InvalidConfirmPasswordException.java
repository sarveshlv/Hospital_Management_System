package com.Capg.User.Exception;

public class InvalidConfirmPasswordException extends RuntimeException{

    public InvalidConfirmPasswordException(String msg) {
        super(msg);
    }
}
