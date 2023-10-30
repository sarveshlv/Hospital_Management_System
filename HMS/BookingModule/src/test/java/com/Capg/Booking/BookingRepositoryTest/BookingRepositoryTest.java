package com.Capg.Booking.BookingRepositoryTest;

import com.Capg.Booking.Constants.BedType;
import com.Capg.Booking.Constants.BookingStatus;
import com.Capg.Booking.Model.Booking;
import com.Capg.Booking.Repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookingRepositoryTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private MongoTemplate mongoTemplate;

    @Test
    public void testFindAllByPatientId() throws ParseException {

        List<Booking> bookings = new ArrayList<>();
        bookings.add(createBooking());
        when(mongoTemplate.find(any(Query.class), eq(Booking.class))).thenReturn(bookings);
        List<Booking> result = bookingRepository.findAllByPatientId("pat123");
        assertNotNull(result);
        assertEquals("book123", bookings.get(0).getBookingId());

    }

    @Test
    public void testFindAllByHospitalId() throws ParseException {

        List<Booking> bookings = new ArrayList<>();
        bookings.add(createBooking());
        when(mongoTemplate.find(any(Query.class), eq(Booking.class))).thenReturn(bookings);
        List<Booking> result = bookingRepository.findAllByHospitalId("abcd123");
        assertNotNull(result);
        assertEquals("abcd1234", bookings.get(0).getHospitalId());

    }

    private static Booking createBooking() throws ParseException {
        Booking obj = new Booking();
        obj.setBookingId("book123");
        obj.setPatientId("pat123");
        obj.setHospitalId("abcd1234");
        obj.setBedId("bed123");
        obj.setBedType(BedType.REGULAR_BED);
        obj.setBookingDate(getDate("2023-09-12"));
        obj.setFromDate(getDate("2023-09-14"));
        obj.setToDate(getDate("2023-09-16"));
        obj.setBookingStatus(BookingStatus.REQUESTED);
        return obj;
    }

    private static Date getDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = format.parse(date);
        return date1;
    }
}
