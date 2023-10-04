package com.hms.bookingms.service;

import com.hms.bookingms.clients.IBedServiceClient;
import com.hms.bookingms.clients.IHospitalServiceClient;
import com.hms.bookingms.clients.IPatientServiceClient;
import com.hms.bookingms.dto.AddBookingRequest;
import com.hms.bookingms.dto.Bed;
import com.hms.bookingms.entities.Booking;
import com.hms.bookingms.exceptions.*;
import com.hms.bookingms.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

	@Mock
	private IBedServiceClient bedServiceClient;

	@Mock
	private IPatientServiceClient patientServiceClient;

	@Mock
	private IHospitalServiceClient hospitalServiceClient;

	@Mock
	private BookingRepository bookingRepository;

	@InjectMocks
	private BookingService bookingService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	// Test case for adding a booking with valid request
	@Test
	void testAddBooking_ValidRequest()
			throws PatientNotFoundException, HospitalNotFoundException, InvalidDatesException {
		// Arrange
		AddBookingRequest addBookingRequest = createValidAddBookingRequest();
		Booking savedBooking = createSampleBooking();

		// Mock
		when(patientServiceClient.isPatientFound(anyString(), anyString())).thenReturn(ResponseEntity.ok().build());
		when(hospitalServiceClient.isHospitalFound(anyString(), anyString())).thenReturn(ResponseEntity.ok().build());
		when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

		// Act
		Booking result = bookingService.addBooking("AuthorizationHeader", addBookingRequest);

		// Assert
		assertNotNull(result);
		assertEquals(savedBooking, result);
		assertEquals(Booking.BookingStatus.REQUESTED, result.getBookingStatus());
	}

	// Test case for adding a booking when patient is not found
	@Test
	void testAddBooking_PatientNotFound() throws PatientNotFoundException {
		// Arrange
		AddBookingRequest addBookingRequest = createValidAddBookingRequest();

		// Mock
		when(patientServiceClient.isPatientFound(anyString(), anyString()))
				.thenReturn(ResponseEntity.notFound().build());

		// Act and Assert
		assertThrows(PatientNotFoundException.class,
				() -> bookingService.addBooking("AuthorizationHeader", addBookingRequest));
	}

	// Test case for adding a booking when hospital is not found
	@Test
	void testAddBooking_HospitalNotFound() throws PatientNotFoundException, HospitalNotFoundException {
		// Arrange
		AddBookingRequest addBookingRequest = createValidAddBookingRequest();

		// Mock
		when(patientServiceClient.isPatientFound(anyString(), anyString())).thenReturn(ResponseEntity.ok().build());
		when(hospitalServiceClient.isHospitalFound(anyString(), anyString()))
				.thenReturn(ResponseEntity.notFound().build());

		// Act and Assert
		assertThrows(HospitalNotFoundException.class,
				() -> bookingService.addBooking("AuthorizationHeader", addBookingRequest));
	}

	// Test case for adding a booking with invalid dates
	@Test
	void testAddBooking_InvalidDates() throws PatientNotFoundException, HospitalNotFoundException {
		// Arrange
		AddBookingRequest addBookingRequest = createInvalidDateAddBookingRequest();

		// Mock
		when(patientServiceClient.isPatientFound(anyString(), anyString())).thenReturn(ResponseEntity.ok().build());
		when(hospitalServiceClient.isHospitalFound(anyString(), anyString())).thenReturn(ResponseEntity.ok().build());

		// Act and Assert
		assertThrows(InvalidDatesException.class,
				() -> bookingService.addBooking("AuthorizationHeader", addBookingRequest));
	}

	// Test case for finding a booking by ID when the booking exists
	@Test
	void testFindBookingById_BookingFound() throws BookingNotFoundException {
		// Arrange
		Booking sampleBooking = createSampleBooking();
		when(bookingRepository.findById(anyString())).thenReturn(java.util.Optional.of(sampleBooking));

		// Act
		Booking result = bookingService.findBookingById("bookingId");

		// Assert
		assertNotNull(result);
		assertEquals(sampleBooking, result);
	}

	// Test case for finding a booking by ID when the booking does not exist
	@Test
	void testFindBookingById_BookingNotFound() {
        // Arrange
        when(bookingRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(BookingNotFoundException.class, () -> bookingService.findBookingById("nonexistentBookingId"));
    }

	// Test case for getting bookings by patient ID when the patient exists
	@Test
	void testGetBookingByPatientId_PatientFound() throws PatientNotFoundException {
		// Arrange
		List<Booking> sampleBookings = createSampleBookingList();

		// Mock
		when(bookingRepository.findAllByPatientId(anyString())).thenReturn(sampleBookings);

		// Act
		List<Booking> result = bookingService.getBookingByPatientId("patientId");

		// Assert
		assertNotNull(result);
		assertEquals(sampleBookings, result);
	}

	// Test case for getting bookings by patient ID when the patient does not exist
	@Test
	void testGetBookingByPatientId_PatientNotFound() {
        // Arrange
        when(bookingRepository.findAllByPatientId(anyString())).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(PatientNotFoundException.class, () -> bookingService.getBookingByPatientId("nonexistentPatientId"));
    }

	// Test case for approving a booking with a valid request
	@Test
	void testApproveBooking_BookingRequested() throws BookingNotFoundException, InvalidBookingRequest {
		// Arrange
		Booking sampleBooking = createSampleBooking();
		Bed sampleBed = createSampleBed();

		// Mock
		when(bookingRepository.findById(anyString())).thenReturn(Optional.of(sampleBooking));
		when(bedServiceClient.findBedsByHospitalId(anyString(), anyString())).thenReturn(List.of(sampleBed));
		when(bedServiceClient.bookBed(anyString(), anyString())).thenReturn(ResponseEntity.ok(sampleBed));

		// Act
		Booking result = bookingService.approveBooking("AuthorizationHeader", "bookingId");

		// Assert
		assertNotNull(result);
		assertEquals(Booking.BookingStatus.APPROVED, result.getBookingStatus());
		assertNotNull(result.getBedId());
	}

	// Test case for approving a booking when the booking is not requested
	@Test
	void testApproveBooking_BookingNotRequested() {
		// Arrange
		Booking sampleBooking = createSampleBooking();
		sampleBooking.setBookingStatus(Booking.BookingStatus.APPROVED);

		when(bookingRepository.findById(anyString())).thenReturn(java.util.Optional.of(sampleBooking));

		// Act and Assert
		assertThrows(InvalidBookingRequest.class,
				() -> bookingService.approveBooking("AuthorizationHeader", "bookingId"));
	}

	// Test case for rejecting a booking with a valid request
	@Test
	void testRejectBooking_BookingRequested() throws BookingNotFoundException, InvalidBookingRequest {
		// Arrange
		Booking sampleBooking = createSampleBooking();

		// Mock
		when(bookingRepository.findById(anyString())).thenReturn(Optional.of(sampleBooking));

		// Act
		Booking result = bookingService.rejectBooking("bookingId");

		// Assert
		assertNotNull(result);
		assertEquals(Booking.BookingStatus.DECLINED, result.getBookingStatus());
	}

	// Test case for rejecting a booking when the booking is not requested
	@Test
	void testRejectBooking_BookingNotRequested() {
		// Arrange
		Booking sampleBooking = createSampleBooking();
		sampleBooking.setBookingStatus(Booking.BookingStatus.APPROVED);

		// Mock
		when(bookingRepository.findById(anyString())).thenReturn(java.util.Optional.of(sampleBooking));

		// Act and Assert
		assertThrows(InvalidBookingRequest.class, () -> bookingService.rejectBooking("bookingId"));
	}

	// Test case for canceling a booking with a valid request
	@Test
	void testCancelBooking_BookingRequested() throws BookingNotFoundException, InvalidBookingRequest {
		// Arrange
		Booking sampleBooking = createSampleBooking();

		// Mock
		when(bookingRepository.findById(anyString())).thenReturn(java.util.Optional.of(sampleBooking));

		// Act
		Booking result = bookingService.cancelBooking("AuthorizationHeader", "bookingId");

		// Assert
		assertNotNull(result);
		assertEquals(Booking.BookingStatus.CANCELLED, result.getBookingStatus());
	}

	// Test case for canceling a booking when the booking is not requested
	@Test
	void testCancelBooking_BookingNotRequested() {
		// Arrange
		Booking sampleBooking = createSampleBooking();
		sampleBooking.setBookingStatus(Booking.BookingStatus.APPROVED);

		// Mock
		when(bookingRepository.findById(anyString())).thenReturn(java.util.Optional.of(sampleBooking));

		// Act and Assert
		assertThrows(InvalidBookingRequest.class,
				() -> bookingService.cancelBooking("AuthorizationHeader", "bookingId"));
	}

	// Test case for completing a booking with a valid request
	@Test
	void testCompleteBooking_BookingApproved() {
		// Arrange
		Booking sampleBooking = createSampleBooking();
		sampleBooking.setBookingStatus(Booking.BookingStatus.APPROVED);
		Bed sampleBed = createSampleBed();

		// Mock
		when(bookingRepository.findById(anyString())).thenReturn(java.util.Optional.of(sampleBooking));
		when(bedServiceClient.completeBooking(anyString(), anyString())).thenReturn(ResponseEntity.ok(sampleBed));

		// Act
		Booking result = bookingService.completeBooking("AuthorizationHeader", "bookingId");

		// Assert
		assertNotNull(result);
		assertEquals(Booking.BookingStatus.COMPLETED, result.getBookingStatus());
	}

	// Test case for completing a booking when the booking is not approved
	@Test
	void testCompleteBooking_BookingNotApproved() {
		// Arrange
		Booking sampleBooking = createSampleBooking();
		sampleBooking.setBookingStatus(Booking.BookingStatus.REQUESTED);

		// Mock
		when(bookingRepository.findById(anyString())).thenReturn(java.util.Optional.of(sampleBooking));

		// Act and Assert
		assertThrows(InvalidBookingRequest.class,
				() -> bookingService.completeBooking("AuthorizationHeader", "bookingId"));
	}

	private AddBookingRequest createValidAddBookingRequest() {
		AddBookingRequest request = new AddBookingRequest();
		request.setPatientId("patientId");
		request.setHospitalId("hospitalId");
		request.setBedType("USUAL_BED");
		request.setOccupyDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
		request.setReleaseDate(new Date(System.currentTimeMillis() + 48 * 60 * 60 * 1000));
		return request;
	}

	private AddBookingRequest createInvalidDateAddBookingRequest() {
		AddBookingRequest request = new AddBookingRequest();
		request.setPatientId("patientId");
		request.setHospitalId("hospitalId");
		request.setBedType("USUAL_BED");
		request.setOccupyDate(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
		request.setReleaseDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
		return request;
	}

	private Booking createSampleBooking() {
		Booking booking = new Booking();
		booking.setId("bookingId");
		booking.setPatientId("patientId");
		booking.setHospitalId("hospitalId");
		booking.setBedType(Booking.BedType.USUAL_BED);
		booking.setBookingDate(new Date());
		booking.setOccupyDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
		booking.setReleaseDate(new Date(System.currentTimeMillis() + 48 * 60 * 60 * 1000));
		booking.setBookingStatus(Booking.BookingStatus.REQUESTED);
		return booking;
	}

	private List<Booking> createSampleBookingList() {
		List<Booking> bookings = new ArrayList<>();
		bookings.add(createSampleBooking());
		return bookings;
	}

	private Bed createSampleBed() {
		Bed bed = new Bed();
		bed.setId("bedId");
		bed.setBedType("USUAL_BED");
		return bed;
	}
}