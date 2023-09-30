package com.hms.bookingms.service;

import java.util.List;

import com.hms.bookingms.dto.AddBookingRequest;
import com.hms.bookingms.entities.Booking;
import com.hms.bookingms.exceptions.BookingNotFoundException;
import com.hms.bookingms.exceptions.HospitalNotFoundException;
import com.hms.bookingms.exceptions.InvalidBookingRequest;
import com.hms.bookingms.exceptions.InvalidDatesException;
import com.hms.bookingms.exceptions.PatientNotFoundException;

public interface IBookingService {
	Booking addBooking(String authorizationHeader, AddBookingRequest addBookingRequest)
			throws PatientNotFoundException, HospitalNotFoundException, InvalidDatesException;

	Booking findBookingById(String bookingId) throws BookingNotFoundException;

	List<Booking> getBookingByPatientId(String patientId) throws PatientNotFoundException;

	List<Booking> getBookingByHospitalId(String hospitalId) throws HospitalNotFoundException;

	Booking approveBooking(String authorizationHeader, String bookingId)
			throws BookingNotFoundException, InvalidBookingRequest;

	Booking rejectBooking(String bookingId) throws BookingNotFoundException, InvalidBookingRequest;

	Booking cancelBooking(String authorizationHeader, String bookingId)
			throws BookingNotFoundException, InvalidBookingRequest;

	Booking completeBooking(String authorizationHeader, String bookingId) throws BookingNotFoundException;
}