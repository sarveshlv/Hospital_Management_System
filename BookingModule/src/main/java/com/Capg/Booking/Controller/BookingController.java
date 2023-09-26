package com.Capg.Booking.Controller;

import com.Capg.Booking.DTO.AddBookingDTO;
import com.Capg.Booking.Exception.*;
import com.Capg.Booking.Model.Booking;
import com.Capg.Booking.Service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/addBooking")
    public Booking addBooking(@Valid @RequestBody AddBookingDTO addBookingDTO) throws InvalidDatesException {
        return bookingService.addBooking(addBookingDTO);
    }

    @GetMapping("/findBooking/{bookingId}")
    public Booking getBookingById(@PathVariable String bookingId) throws BookingNotFoundException {
        return bookingService.findBookingById(bookingId);
    }

    @GetMapping("/findByPatientId/{patientId}")
    public List<Booking> getBookingsByPatientId(@PathVariable String patientId) throws PatientNotFoundException {
        return bookingService.getBookingByPatientId(patientId);
    }

    @GetMapping("/findByHospitalId/{hospitalId}")
    public List<Booking> getBookingByHospitalId(@PathVariable String hospitalId) throws HospitalNotFoundException {
        return bookingService.getBookingByHospitalId(hospitalId);
    }

    @GetMapping("/cancel/{bookingId}")
    public Booking cancelBooking(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String bookingId)
            throws BookingNotFoundException, InvalidBookingRequest {
        return bookingService.cancelBooking(authorizationHeader, bookingId);
    }

}
