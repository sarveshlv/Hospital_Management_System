package com.Capg.Booking.Service;

import com.Capg.Booking.Constants.BedType;
import com.Capg.Booking.Constants.BookingStatus;
import com.Capg.Booking.DTO.AddBookingDTO;
import com.Capg.Booking.Exception.*;
import com.Capg.Booking.Model.Booking;
import com.Capg.Booking.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public Booking addBooking(AddBookingDTO addBookingDTO) {

        String patientId = addBookingDTO.getPatientId();
        String hospitalId = addBookingDTO.getHospitalId();

        Date bookingDate = new Date();
        Date fromDate = addBookingDTO.getFromDate();
        Date toDate = addBookingDTO.getToDate();

        if (fromDate.before(bookingDate) || toDate.before(fromDate)) {
            throw new InvalidDatesException(bookingDate, fromDate, toDate);
        }

        Booking obj = new Booking();
        obj.setPatientId(patientId);
        obj.setHospitalId(hospitalId);
        obj.setBedType(BedType.valueOf(addBookingDTO.getBedType()));
        obj.setBookingDate(bookingDate);
        obj.setFromDate(fromDate);
        obj.setToDate(toDate);
        obj.setBookingStatus(BookingStatus.REQUESTED);

        return bookingRepository.save(obj);

    }

    @Override
    public Booking findBookingById(String bookingId) throws BookingNotFoundException {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(bookingId));
    }

    @Override
    public List<Booking> getBookingByPatientId(String patientId) {
        List<Booking> bookings = bookingRepository.findAllByPatientId(patientId);
        if(bookings.isEmpty()) {
            throw new PatientNotFoundException(patientId);
        }
        return bookings;
    }

    @Override
    public Booking cancelBooking(String authorizationHeader, String bookingId) throws BookingNotFoundException, InvalidBookingRequest {
        Booking booking = findBookingById(bookingId);
        if (booking.getBookingStatus().equals(BookingStatus.APPROVED)) {
            booking.setBookingStatus(BookingStatus.CANCELLED);
//            bedService.cancelBedBooking(booking.getBedId());
            return bookingRepository.save(booking);
        }
        throw new InvalidBookingRequest(booking.getBookingStatus().toString());
    }

    @Override
    public List<Booking> getBookingByHospitalId(String hospitalId) throws HospitalNotFoundException {
        List<Booking> bookings = bookingRepository.findAllByHospitalId(hospitalId);
        if (bookings.isEmpty()) {
            throw new HospitalNotFoundException(hospitalId);
        }
        return bookings;
    }
}
