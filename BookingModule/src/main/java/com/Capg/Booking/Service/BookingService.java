package com.Capg.Booking.Service;

import com.Capg.Booking.DTO.AddBookingDTO;
import com.Capg.Booking.Exception.BookingNotFoundException;
import com.Capg.Booking.Exception.HospitalNotFoundException;
import com.Capg.Booking.Exception.InvalidBookingRequest;
import com.Capg.Booking.Model.Booking;

import java.util.List;

public interface BookingService {


    Booking addBooking(AddBookingDTO addBookingDTO);

    Booking findBookingById(String bookingId) throws BookingNotFoundException;

    List<Booking> getBookingByPatientId(String patientId);

    Booking cancelBooking(String authorizationHeader, String bookingId) throws BookingNotFoundException, InvalidBookingRequest;

    List<Booking> getBookingByHospitalId(String hospitalId) throws HospitalNotFoundException;
}
