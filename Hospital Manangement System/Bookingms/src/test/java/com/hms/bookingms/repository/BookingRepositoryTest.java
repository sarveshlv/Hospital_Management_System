package com.hms.bookingms.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.hms.bookingms.entities.Booking;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void testSaveBooking() {
        // Create a Booking object
        Booking booking = new Booking();
        booking.setPatientId("patient123");
        booking.setHospitalId("hospital456");
        booking.setBedId("bed789");
        booking.setBedType(Booking.BedType.USUAL_BED);
        booking.setBookingDate(new Date());
        booking.setBookingStatus(Booking.BookingStatus.REQUESTED);

        // Save the booking
        Booking savedBooking = bookingRepository.save(booking);

        // Retrieve the saved booking
        Optional<Booking> retrievedBooking = bookingRepository.findById(savedBooking.getId());

        // Check if the retrieved booking matches the saved booking
        assertTrue(retrievedBooking.isPresent());
    }
    
    @AfterEach
    public void cleanup() {
        // Delete all records from the Booking collection
        bookingRepository.deleteAll();
    }
    @Test
    public void testFindAllByPatientId() {
        // Create multiple bookings with the same patientId
        Booking booking1 = new Booking();
        booking1.setPatientId("patient123");
        booking1.setHospitalId("hospital456");
        booking1.setBedId("bed789");
        booking1.setBedType(Booking.BedType.USUAL_BED);
        booking1.setBookingDate(new Date());
        booking1.setBookingStatus(Booking.BookingStatus.REQUESTED);

        Booking booking2 = new Booking();
        booking2.setPatientId("patient123");
        booking2.setHospitalId("hospital789");
        booking2.setBedId("bed101");
        booking2.setBedType(Booking.BedType.ICU_BED);
        booking2.setBookingDate(new Date());
        booking2.setBookingStatus(Booking.BookingStatus.APPROVED);

        
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

        // Retrieve bookings by patientId
        List<Booking> bookings = bookingRepository.findAllByPatientId("patient123");

        // Check if both bookings are retrieved
        assertEquals(2, bookings.size());
    }
}