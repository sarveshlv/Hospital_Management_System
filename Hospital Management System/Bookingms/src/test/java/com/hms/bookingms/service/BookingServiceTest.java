package com.hms.bookingms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.hms.bookingms.clients.IBedServiceClient;
import com.hms.bookingms.clients.IHospitalServiceClient;
import com.hms.bookingms.clients.IPatientServiceClient;
import com.hms.bookingms.dto.AddBookingRequest;
import com.hms.bookingms.entities.Booking;
import com.hms.bookingms.exceptions.*;
import com.hms.bookingms.repository.BookingRepository;

public class BookingServiceTest {

	@InjectMocks
	private BookingService bookingService;

	@Mock
	private BookingRepository bookingRepository;

	@Mock
	private IBedServiceClient bedServiceClient;

	@Mock
	private IPatientServiceClient patientServiceClient;

	@Mock
	private IHospitalServiceClient hospitalServiceClient;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testAddBooking_ValidRequest_Success() throws Exception {
		// Arrange
		AddBookingRequest request = new AddBookingRequest("patientId", "hospitalId", "USUAL_BED",
				new Date(System.currentTimeMillis() + 3600000), new Date(System.currentTimeMillis() + 7200000));
		when(patientServiceClient.isPatientFound(request.getPatientId())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
		when(hospitalServiceClient.isHospitalFound(request.getHospitalId())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
		when(bookingRepository.save(any(Booking.class))).thenReturn(new Booking());

		// Act
		Booking result = bookingService.addBooking(request);

		// Assert
		assertNotNull(result);
		assertEquals(Booking.BookingStatus.REQUESTED, result.getBookingStatus());
	}

	@Test
	public void testAddBooking_InvalidPatient_ThrowsPatientNotFoundException() {
		// Arrange
		AddBookingRequest request = new AddBookingRequest("nonExistentPatient", "hospitalId", "USUAL_BED",
				new Date(System.currentTimeMillis() + 3600000), new Date(System.currentTimeMillis() + 7200000));
		when(patientServiceClient.isPatientFound(request.getPatientId())).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

		// Act & Assert
		assertThrows(PatientNotFoundException.class, () -> bookingService.addBooking(request));
	}

	@Test
	public void testAddBooking_InvalidHospital_ThrowsHospitalNotFoundException() {
		// Arrange
		AddBookingRequest request = new AddBookingRequest("patientId", "nonExistentHospital", "USUAL_BED",
				new Date(System.currentTimeMillis() + 3600000), new Date(System.currentTimeMillis() + 7200000));
		when(patientServiceClient.isPatientFound(request.getPatientId())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
		when(hospitalServiceClient.isHospitalFound(request.getHospitalId())).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

		// Act & Assert
		assertThrows(HospitalNotFoundException.class, () -> bookingService.addBooking(request));
	}

	@Test
	public void testAddBooking_InvalidDates_ThrowsInvalidDatesException() {
		// Arrange
		AddBookingRequest request = new AddBookingRequest("patientId", "hospitalId", "USUAL_BED",
				new Date(System.currentTimeMillis() - 3600000), new Date(System.currentTimeMillis() + 3600000));
		when(patientServiceClient.isPatientFound(request.getPatientId())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));
		when(hospitalServiceClient.isHospitalFound(request.getHospitalId())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

		// Act & Assert
		assertThrows(InvalidDatesException.class, () -> bookingService.addBooking(request));
	}

	@Test
	public void testFindBookingById_ValidId_ReturnsBooking() throws Exception {
		// Arrange
		String bookingId = "validBookingId";
		Booking mockBooking = new Booking();
		when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(mockBooking));

		// Act
		Booking result = bookingService.findBookingById(bookingId);

		// Assert
		assertNotNull(result);
		assertEquals(mockBooking, result);
	}

	@Test
	public void testFindBookingById_InvalidId_ThrowsBookingNotFoundException() {
		// Arrange
		String invalidBookingId = "invalidBookingId";
		when(bookingRepository.findById(invalidBookingId)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(BookingNotFoundException.class, () -> bookingService.findBookingById(invalidBookingId));
	}

	@Test
	public void testGetBookingByPatientId_ValidPatientId_ReturnsBookingList() throws Exception {
		// Arrange
		String patientId = "validPatientId";
		List<Booking> mockBookings = new ArrayList<>();
		mockBookings.add(new Booking());
		when(bookingRepository.findAllByPatientId(patientId)).thenReturn(mockBookings);

		// Act
		List<Booking> result = bookingService.getBookingByPatientId(patientId);

		// Assert
		assertNotNull(result);
		assertEquals(mockBookings, result);
	}

	@Test
	public void testGetBookingByPatientId_InvalidPatientId_ThrowsPatientNotFoundException() {
		// Arrange
		String invalidPatientId = "invalidPatientId";
		when(bookingRepository.findAllByPatientId(invalidPatientId)).thenReturn(new ArrayList<>());

		// Act & Assert
		assertThrows(PatientNotFoundException.class, () -> bookingService.getBookingByPatientId(invalidPatientId));
	}

	@Test
	public void testApproveBooking_ValidBookingId_ReturnsApprovedBooking() throws Exception {
		// Arrange
		String bookingId = "validBookingId";
		Booking booking = new Booking();
		booking.setBookingStatus(Booking.BookingStatus.REQUESTED);
		when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
		when(bedServiceClient.bookBed(any())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

		// Act
		Booking result = bookingService.approveBooking(bookingId);

		// Assert
		assertNotNull(result);
		assertEquals(Booking.BookingStatus.APPROVED, result.getBookingStatus());
		assertEquals("validBedId", result.getBedId());
	}

	@Test
	public void testApproveBooking_InvalidBookingStatus_ThrowsInvalidBookingRequest() {
		// Arrange
		String bookingId = "validBookingId";
		Booking booking = new Booking();
		booking.setBookingStatus(Booking.BookingStatus.APPROVED);
		when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

		// Act & Assert
		assertThrows(InvalidBookingRequest.class, () -> bookingService.approveBooking(bookingId));
	}

	@Test
	public void testRejectBooking_ValidBookingId_ReturnsDeclinedBooking() throws Exception {
		// Arrange
		String bookingId = "validBookingId";
		Booking booking = new Booking();
		booking.setBookingStatus(Booking.BookingStatus.REQUESTED);
		when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

		// Act
		Booking result = bookingService.rejectBooking(bookingId);

		// Assert
		assertNotNull(result);
		assertEquals(Booking.BookingStatus.DECLINED, result.getBookingStatus());
	}

	@Test
	public void testRejectBooking_InvalidBookingStatus_ThrowsInvalidBookingRequest() {
		// Arrange
		String bookingId = "validBookingId";
		Booking booking = new Booking();
		booking.setBookingStatus(Booking.BookingStatus.APPROVED);
		when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

		// Act & Assert
		assertThrows(InvalidBookingRequest.class, () -> bookingService.rejectBooking(bookingId));
	}

	@Test
	public void testCancelBooking_ValidBookingId_ReturnsCancelledBooking() throws Exception {
		// Arrange
		String bookingId = "validBookingId";
		Booking booking = new Booking();
		booking.setBookingStatus(Booking.BookingStatus.APPROVED);
		booking.setBedId("validBedId");
		when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
		when(bedServiceClient.unbookBed(any(String.class))).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

		// Act
		Booking result = bookingService.cancelBooking(bookingId);

		// Assert
		assertNotNull(result);
		assertEquals(Booking.BookingStatus.CANCELLED, result.getBookingStatus());
	}

	@Test
	public void testCancelBooking_InvalidBookingStatus_ThrowsInvalidBookingRequest() {
		// Arrange
		String bookingId = "validBookingId";
		Booking booking = new Booking();
		booking.setBookingStatus(Booking.BookingStatus.REQUESTED);
		when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

		// Act & Assert
		assertThrows(InvalidBookingRequest.class, () -> bookingService.cancelBooking(bookingId));
	}

}