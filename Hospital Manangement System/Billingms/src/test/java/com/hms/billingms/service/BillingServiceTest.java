package com.hms.billingms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hms.billingms.clients.IBedServiceClient;
import com.hms.billingms.clients.IBookingServiceClient;
import com.hms.billingms.dto.Bed;
import com.hms.billingms.dto.Booking;
import com.hms.billingms.entities.Billing;
import com.hms.billingms.exceptions.BillingNotFoundException;
import com.hms.billingms.exceptions.BookingNotFoundException;

import com.hms.billingms.repository.BillingRepository;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BillingServiceTest {

	@Mock
	private BillingRepository billingRepository;

	@Mock
	private IBookingServiceClient bookingServiceClient;

	@Mock
	private IBedServiceClient bedServiceClient;

	@InjectMocks
	private BillingService billingService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

    // Test the generation of a billing for a valid request.
	@Test
	public void testGenerateBill_ValidRequest_ReturnsBilling() throws BookingNotFoundException {
		// Arrange
		String authorizationHeader = "validAuthorizationHeader";
		String bookingId = "validBookingId";
		Booking booking = new Booking();
	    booking.setReleaseDate(new Date(System.currentTimeMillis() + 48 * 60 * 60 * 1000));
	    booking.setOccupyDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
	    
		Bed bed = new Bed();
		bed.setCostPerDay(50.0);

		// Mocking behavior of bookingServiceClient and bedServiceClient
		when(bookingServiceClient.completeBooking(authorizationHeader, bookingId)).thenReturn(booking);
		when(bedServiceClient.makeBedAvaialbe(authorizationHeader, booking.getBedId())).thenReturn(bed);

		
	    // Arrange
	    Billing billing = new Billing();
	    billing.setBookingId(bookingId);

	    // Mock
	    when(billingService.addBill(any(Billing.class))).thenReturn(billing);

		// Act
	    Billing resultBilling = billingService.generateBill(authorizationHeader, bookingId);

	    // Assert
	    assertNotNull(resultBilling);
	    assertEquals(bookingId, resultBilling.getBookingId());
	}

    // Test that an BookingNotFoundException is thrown when the booking is invalid.
	@Test
	public void testGenerateBill_InvalidBooking_ThrowsBookingNotFoundException() throws BookingNotFoundException {
		// Arrange
		String authorizationHeader = "validAuthorizationHeader";
		String bookingId = "invalidBookingId";

		// Mocking behavior of bookingServiceClient to throw BookingNotFoundException
		when(bookingServiceClient.completeBooking(authorizationHeader, bookingId))
				.thenThrow(BookingNotFoundException.class);

		// Act and Assert
		assertThrows(BookingNotFoundException.class, () -> billingService.generateBill(authorizationHeader, bookingId));
	}

    // Test that an BookingNotFoundException is thrown when the bed is invalid.
	@Test
	public void testGenerateBill_InvalidBed_ThrowsBedNotFoundException() throws BookingNotFoundException {
		// Arrange
		String authorizationHeader = "validAuthorizationHeader";
		String bookingId = "validBookingId";
		Booking booking = new Booking();

		// Mocking behavior of bookingServiceClient
		when(bookingServiceClient.completeBooking(authorizationHeader, bookingId)).thenReturn(booking);

		// Mocking behavior of bedServiceClient to throw BedNotFoundException
		when(bedServiceClient.makeBedAvaialbe(authorizationHeader, booking.getBedId()))
				.thenThrow(BookingNotFoundException.class);

		// Act and Assert
		assertThrows(BookingNotFoundException.class, () -> billingService.generateBill(authorizationHeader, bookingId));
	}

    // Test the addition of a valid billing and returning the saved billing.
	@Test
	public void testAddBill_ValidBilling_ReturnsSavedBilling() {
		// Arrange
		Billing billing = new Billing();
		billing.setBookingId("validBookingId");
		billing.setBillAmount(100.0);
		billing.setPaymentStatus(Billing.PaymentStatus.PENDING);

		// Mock
		when(billingRepository.save(billing)).thenReturn(billing);

		// Act
		Billing savedBilling = billingService.addBill(billing);

		// Assert
		assertNotNull(savedBilling);
		assertEquals("validBookingId", savedBilling.getBookingId());
		assertEquals(100.0, savedBilling.getBillAmount());
		assertEquals(Billing.PaymentStatus.PENDING, savedBilling.getPaymentStatus());
	}

    // Test the retrieval of a billing by a valid billing ID.
	@Test
	public void testFindById_ValidBillingId_ReturnsBilling() throws BillingNotFoundException {
		// Arrange
		String billingId = "validBillingId";

		// Mock
		Billing billing = new Billing();
		billing.setId(billingId);
		billing.setBookingId("validBookingId");
		billing.setBillAmount(100.0);
		billing.setPaymentStatus(Billing.PaymentStatus.PENDING);
		when(billingRepository.findById(billingId)).thenReturn(Optional.of(billing));

		// Act
		Billing foundBilling = billingService.findById(billingId);

		// Assert
		assertNotNull(foundBilling);
		assertEquals(billingId, foundBilling.getId());
		assertEquals("validBookingId", foundBilling.getBookingId());
		assertEquals(100.0, foundBilling.getBillAmount());
		assertEquals(Billing.PaymentStatus.PENDING, foundBilling.getPaymentStatus());
	}

    // Test that an exception is thrown when an invalid billing ID is used.
	@Test
	public void testFindById_InvalidBillingId_ThrowsBillingNotFoundException() {
		// Arrange
		String invalidBillingId = "invalidBillingId";

		// Mock
		when(billingRepository.findById(invalidBillingId)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(BillingNotFoundException.class, () -> billingService.findById(invalidBillingId));
	}

    // Test the retrieval of a billing by a valid booking ID.
	@Test
	public void testFindByBookingId_ValidBookingId_ReturnsBilling() throws BookingNotFoundException {
		// Arrange
		String bookingId = "validBookingId";

		// Mock
		Billing billing = new Billing();
		billing.setBookingId(bookingId);
		billing.setBillAmount(100.0);
		billing.setPaymentStatus(Billing.PaymentStatus.PENDING);
		when(billingRepository.findByBookingId(bookingId)).thenReturn(Optional.of(billing));

		// Act
		Billing foundBilling = billingService.findByBookingId(bookingId);

		// Assert
		assertNotNull(foundBilling);
		assertEquals(bookingId, foundBilling.getBookingId());
		assertEquals(100.0, foundBilling.getBillAmount());
		assertEquals(Billing.PaymentStatus.PENDING, foundBilling.getPaymentStatus());
	}

    // Test that an BookingNotFoundException is thrown when an invalid booking ID is used.
	@Test
	public void testFindByBookingId_InvalidBookingId_ThrowsBookingNotFoundException() {
		// Arrange
		String invalidBookingId = "invalidBookingId";

		// Mock
		when(billingRepository.findByBookingId(invalidBookingId)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(BookingNotFoundException.class, () -> billingService.findByBookingId(invalidBookingId));
	}
}