package com.Capg.Booking.Service;

import com.Capg.Booking.DTO.AddBookingDTO;
import com.Capg.Booking.Exception.BookingNotFoundException;
import com.Capg.Booking.Exception.HospitalNotFoundException;
import com.Capg.Booking.Exception.InvalidBookingRequest;
import com.Capg.Booking.Model.Booking;

import java.text.ParseException;
import java.util.List;

public interface BookingService {


    Booking addBooking(String authroizationHeader, AddBookingDTO addBookingDTO) throws ParseException;

    Booking findBookingById(String bookingId) throws BookingNotFoundException;

    List<Booking> getBookingByPatientId(String patientId);

    Booking cancelBooking(String authorizationHeader, String bookingId);

    List<Booking> getBookingByHospitalId(String hospitalId) throws HospitalNotFoundException;

    Booking approveBooking(String bookingId);

    Booking declineBooking(String authroizationHeader, String bookingId);

    Booking completeBooking(String bookingId);
}
