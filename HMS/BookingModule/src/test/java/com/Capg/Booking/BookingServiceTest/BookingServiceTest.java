package com.Capg.Booking.BookingServiceTest;

import com.Capg.Booking.Constants.BedType;
import com.Capg.Booking.Constants.BookingStatus;
import com.Capg.Booking.DTO.AddBookingDTO;
import com.Capg.Booking.DTO.Bed;
import com.Capg.Booking.Exception.HospitalNotFoundException;
import com.Capg.Booking.FeignClients.BedServiceClient;
import com.Capg.Booking.Model.Booking;
import com.Capg.Booking.Repository.BookingRepository;
import com.Capg.Booking.Service.BookingServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BedServiceClient bedServiceClient;

    @Test
    public void testAddBooking() throws ParseException {

        String authorizationHeader = "Bearer accessToken";
        AddBookingDTO addBookingDTO = createAddBookingDTO();

        Bed bed = new Bed();
        bed.setBedId("bed123");
        bed.setBedType(String.valueOf(BedType.REGULAR_BED));
        bed.setHospitalId("abcd1234");
        bed.setCostPerDay(100.0);

        Booking booking = createBooking();

        when(bedServiceClient.findBedsByType(authorizationHeader, "REGULAR_BED")).thenReturn(Collections.singletonList(bed));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.addBooking(authorizationHeader, addBookingDTO);
        assertNotNull(result);
        assertEquals("pat123", result.getPatientId());
        assertEquals("bed123", result.getBedId());
    }

    @Test
    public void testFindBookingById() throws ParseException {
        Booking booking = createBooking();
        when(bookingRepository.findById(anyString())).thenReturn(Optional.of(booking));
        Booking result = bookingService.findBookingById("book123");
        assertNotNull(result);
        assertEquals(booking.getBookingId(), result.getBookingId());
    }

    @Test
    public void testGetBookingByPatientId() throws ParseException {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(createBooking());
        when(bookingRepository.findAllByPatientId(anyString())).thenReturn(bookings);
        List<Booking> result = bookingService.getBookingByPatientId("pat123");
        assertNotNull(result);
        assertEquals(bookings, result);
    }

    @Test
    public void testCancelBooking() throws ParseException {

        String header = "Bearer accessToken";
        String bookingId = "book123";
        // Create a mock Booking object
        Booking booking = createBooking();

        when(bookingRepository.findById(anyString())).thenReturn(Optional.of(booking));
        when(bedServiceClient.makeBedAvailable(anyString(), anyString())).thenReturn(new Bed());
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        Booking result = bookingService.cancelBooking(header, "book123");

        assertNotNull(result);
        assertEquals(BookingStatus.CANCELLED, result.getBookingStatus());
        verify(bookingRepository, times(1)).findById(booking.getBookingId());
    }

    @Test
    public void testGetBookingByHospitalId() throws HospitalNotFoundException, ParseException {
        // Define test data
        String hospitalId = "abcd1234";
        List<Booking> bookings = new ArrayList<>();
        Booking booking = createBooking();
        bookings.add(booking);

        when(bookingRepository.findAllByHospitalId(hospitalId)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingByHospitalId(hospitalId);

        assertNotNull(result);
        assertEquals(bookings, result);

    }

    @Test
    public void testApproveBooking() throws ParseException {
        // Define test data
        String bookingId = "book123";
        Booking booking = createBooking();

        when(bookingRepository.findById(booking.getBookingId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.approveBooking(bookingId);

        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getBookingStatus());
    }

    @Test
    public void testDeclineBooking() throws ParseException {
        String authorizationHeader = "Bearer accessToken";
        String bookingId = "book123";
        Booking booking = createBooking();

        // Mock behavior of findBookingById and bedServiceClient.makeBedAvailable
        when(bookingRepository.findById(booking.getBookingId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.declineBooking(authorizationHeader, bookingId);

        assertNotNull(result);
        assertEquals(BookingStatus.DECLINED, result.getBookingStatus());

    }

    @Test
    public void testCompleteBooking() throws ParseException {
        // Define test data
        String bookingId = "book123";
        Booking booking = createBooking();

        when(bookingRepository.findById(booking.getBookingId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.completeBooking(bookingId);

        assertNotNull(result);
        assertEquals(BookingStatus.COMPLETED, result.getBookingStatus());
    }

    private static Booking createBooking() throws ParseException {
        Booking obj = new Booking();
        obj.setBookingId("book123");
        obj.setPatientId("pat123");
        obj.setHospitalId("abcd1234");
        obj.setBedId("bed123");
        obj.setBedType(BedType.REGULAR_BED);
        obj.setBookingDate(getDate("2023-10-03"));
        obj.setFromDate(getDate("2023-10-14"));
        obj.setToDate(getDate("2023-10-16"));
        obj.setBookingStatus(BookingStatus.REQUESTED);
        return obj;
    }

    private static AddBookingDTO createAddBookingDTO() {
        AddBookingDTO obj = new AddBookingDTO();
        obj.setPatientId("pat123");
        obj.setHospitalId("abcd1234");
        obj.setBedType(String.valueOf(BedType.REGULAR_BED));
        obj.setFromDate("2023-10-14");
        obj.setToDate("2023-10-16");
        return obj;
    }

    private static Date getDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = format.parse(date);
        return date1;
    }
}
