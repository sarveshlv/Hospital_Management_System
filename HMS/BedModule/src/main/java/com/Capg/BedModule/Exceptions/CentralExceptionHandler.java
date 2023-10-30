package com.Capg.BedModule.Exceptions;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CentralExceptionHandler {

    @ExceptionHandler({HospitalNotFoundException.class, BedNotFoundException.class, BedStatusInvalidException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    //For handling invalid method arguments
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach((error) -> errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    //For handling Feign Exceptions with customised error message
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException ex) {
        HttpStatus httpStatus = HttpStatus.valueOf(ex.status());

        String errorMessage = ex.getMessage();

        Map<String, String> errors = new HashMap<>();
        errors.put("class", ex.getClass().toString());
        errors.put("timestamp", (new Date()).toString());
        errors.put("status", httpStatus.toString());
        errors.put("method", ex.request().httpMethod().toString());
        errors.put("url", ex.request().url());
        errors.put("error",
                errorMessage.substring(errorMessage.lastIndexOf("error") + 8, errorMessage.lastIndexOf("}") - 1));

        return new ResponseEntity<>(errors, httpStatus);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleValidationErrors(RuntimeException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getClass().getName(), ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }
}
