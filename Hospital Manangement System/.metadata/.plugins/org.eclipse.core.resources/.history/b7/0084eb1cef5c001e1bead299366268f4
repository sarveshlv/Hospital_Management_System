package com.hms.bookingms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hms.bookingms.dto.AddBookingRequest;
import com.hms.bookingms.entities.Booking;
import com.hms.bookingms.exceptions.BookingNotFoundException;
import com.hms.bookingms.exceptions.HospitalNotFoundException;
import com.hms.bookingms.exceptions.InvalidBookingRequest;
import com.hms.bookingms.exceptions.InvalidDatesException;
import com.hms.bookingms.exceptions.PatientNotFoundException;
import com.hms.bookingms.service.BookingService;

import jakarta.validation.Valid;

import java.util.List;

@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@PostMapping("/add")
	public Booking addBooking(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody AddBookingRequest addBookingRequest)
			throws PatientNotFoundException, HospitalNotFoundException, InvalidDatesException {
		return bookingService.addBooking(authorizationHeader, addBookingRequest);
	}

	@GetMapping("/findById/{bookingId}")
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

	@GetMapping("/approve/{bookingId}")
	public Booking approveBooking(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String bookingId)
			throws BookingNotFoundException, InvalidBookingRequest {
		return bookingService.approveBooking(authorizationHeader, bookingId);
	}

	@GetMapping("/reject/{bookingId}")
	public Booking rejectBooking(@PathVariable String bookingId)
			throws BookingNotFoundException, InvalidBookingRequest {
		return bookingService.rejectBooking(bookingId);
	}

	@GetMapping("/cancel/{bookingId}")
	public Booking cancelBooking(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String bookingId)
			throws BookingNotFoundException, InvalidBookingRequest {
		return bookingService.cancelBooking(authorizationHeader, bookingId);
	}

	@GetMapping("/complete/{bookingId}")
	public Booking completeBooking(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String bookingId) throws BookingNotFoundException {
		return bookingService.completeBooking(authorizationHeader, bookingId);
	}
}