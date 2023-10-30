package com.Capg.Booking.Controller;

import com.Capg.Booking.DTO.AddBookingDTO;
import com.Capg.Booking.Exception.*;
import com.Capg.Booking.Model.Booking;
import com.Capg.Booking.Service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/Booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    //Add booking details into the database, returns a booking object
    @PostMapping("/addBooking")
    public Booking addBooking(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody AddBookingDTO addBookingDTO) throws InvalidDatesException, ParseException {
        return bookingService.addBooking(authorizationHeader, addBookingDTO);
    }

    //Find booking using booking ID, returns a booking object
    @GetMapping("/findBooking/{bookingId}")
    public Booking getBookingById(@PathVariable String bookingId) throws BookingNotFoundException {
        return bookingService.findBookingById(bookingId);
    }

    //Find booking using patient ID, returns a booking object
    @GetMapping("/findByPatientId/{patientId}")
    public List<Booking> getBookingsByPatientId(@PathVariable String patientId) throws PatientNotFoundException {
        return bookingService.getBookingByPatientId(patientId);
    }

    //Find booking using hospital ID, returns a list of booking objects
    @GetMapping("/findByHospitalId/{hospitalId}")
    public List<Booking> getBookingByHospitalId(@PathVariable String hospitalId) throws HospitalNotFoundException {
        return bookingService.getBookingByHospitalId(hospitalId);
    }

    //Cancel booking using booking ID, returns the updated booking object
    @PutMapping("/cancelBooking/{bookingId}")
    public Booking cancelBooking(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String bookingId) {
        return bookingService.cancelBooking(authorizationHeader, bookingId);
    }

    //Approve booking using booking ID, returns the updated booking object
    @PutMapping("/approveBooking/{bookingId}")
    public Booking approveBooking(@PathVariable String bookingId) {
        return bookingService.approveBooking(bookingId);
    }

    //Decline booking using booking ID, returns the updated booking object
    @PutMapping("/declineBooking/{bookingId}")
    public Booking declineBooking(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String bookingId) {
        return bookingService.declineBooking(authorizationHeader, bookingId);
    }

    //Complete booking using booking ID, returns the updated booking object
    @PutMapping("/completeBooking/{bookingId}")
    public Booking completeBooking(@PathVariable String bookingId) {
        return bookingService.completeBooking(bookingId);
    }
}
