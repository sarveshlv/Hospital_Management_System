package com.Capg.Booking.Service;

import com.Capg.Booking.Constants.BedType;
import com.Capg.Booking.Constants.BookingStatus;
import com.Capg.Booking.DTO.AddBookingDTO;
import com.Capg.Booking.DTO.Bed;
import com.Capg.Booking.Exception.*;
import com.Capg.Booking.FeignClients.BedServiceClient;
import com.Capg.Booking.Model.Booking;
import com.Capg.Booking.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BedServiceClient bedServiceClient;

    @Override
    public Booking addBooking(String authorizationHeader, AddBookingDTO addBookingDTO) throws ParseException {

        String patientId = addBookingDTO.getPatientId();
        String hospitalId = addBookingDTO.getHospitalId();

        Date bookingDate = new Date();
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        Date fromDate = sdf.parse(addBookingDTO.getFromDate());
        Date toDate = sdf.parse(addBookingDTO.getToDate());

        if (fromDate.before(bookingDate) || toDate.before(fromDate)) {
            throw new InvalidDatesException(bookingDate, fromDate, toDate);
        }

        //Get all beds and filter those records by hospitalId and bedType
        List<Bed> list = bedServiceClient.findBedsByType(authorizationHeader, addBookingDTO.getBedType());
        List<Bed> filteredList = list.stream()
                .filter(bed -> bed.getHospitalId().equals(hospitalId) && bed.getBedType().equals(addBookingDTO.getBedType()))
                .collect(Collectors.toList());

        Bed bed = filteredList.get(0);

        //Populate booking obj for adding to the database
        Booking obj = new Booking();
        obj.setPatientId(patientId);
        obj.setHospitalId(hospitalId);
        obj.setBedId(bed.getBedId());
        obj.setBedType(BedType.valueOf(addBookingDTO.getBedType()));
        obj.setBookingDate(bookingDate);
        obj.setFromDate(fromDate);
        obj.setToDate(toDate);
        obj.setBookingStatus(BookingStatus.REQUESTED);

        bedServiceClient.bookBed(authorizationHeader, bed.getBedId());

        return bookingRepository.save(obj);

    }

    @Override
    public Booking findBookingById(String bookingId) throws BookingNotFoundException {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(bookingId));
    }

    @Override
    public List<Booking> getBookingByPatientId(String patientId) {
        List<Booking> bookings = bookingRepository.findAllByPatientId(patientId);
        if (bookings.isEmpty()) {
            throw new BookingNotFoundException("No Bookings done yet!");
        }
        return bookings;
    }

    @Override
    public Booking cancelBooking(String authorizationHeader, String bookingId) {
        Booking booking = findBookingById(bookingId);
        booking.setBookingStatus(BookingStatus.CANCELLED);
        String bedId = booking.getBedId();
        Bed obj = bedServiceClient.makeBedAvailable(authorizationHeader, bedId);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingByHospitalId(String hospitalId) throws HospitalNotFoundException {
        List<Booking> bookings = bookingRepository.findAllByHospitalId(hospitalId);
        if (bookings.isEmpty()) {
            throw new BookingNotFoundException("There are no bookings yet!");
        }
        return bookings;
    }

    @Override
    public Booking approveBooking(String bookingId) {
        Booking booking = findBookingById(bookingId);
        booking.setBookingStatus(BookingStatus.APPROVED);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking declineBooking(String authroizationHeader, String bookingId) {
        Booking booking = findBookingById(bookingId);
        booking.setBookingStatus(BookingStatus.DECLINED);
        String bedId = booking.getBedId();

        Bed obj = bedServiceClient.makeBedAvailable(authroizationHeader, bedId);
        return bookingRepository.save(booking);

    }

    @Override
    public Booking completeBooking(String bookingId) {
        Booking booking = findBookingById(bookingId);
        booking.setBookingStatus(BookingStatus.COMPLETED);
        return bookingRepository.save(booking);
    }
}
