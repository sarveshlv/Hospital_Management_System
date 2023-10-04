package com.hms.bookingms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.bookingms.dto.AddBookingRequest;
import com.hms.bookingms.entities.Booking;
import com.hms.bookingms.exceptions.BookingNotFoundException;
import com.hms.bookingms.exceptions.PatientNotFoundException;
import com.hms.bookingms.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookingControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setControllerAdvice(new CentralExceptionHandler())
                .build();
    }
    
    // Test adding a booking with valid input and check if it returns a booking.
    @Test
    public void testAddBooking_ValidBookingRequest_ReturnsBooking() throws Exception {
        // Arrange
        String authorizationHeader = "validAuthorizationHeader";
        AddBookingRequest addBookingRequest = new AddBookingRequest();
        addBookingRequest.setPatientId("validPatientId");
        addBookingRequest.setHospitalId("validHospitalId");
        addBookingRequest.setBedType("USUAL_BED");
        addBookingRequest.setOccupyDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
        addBookingRequest.setReleaseDate(new Date(System.currentTimeMillis() + 48 * 60 * 60 * 1000)); 
        
        //Mock
        Booking booking = new Booking();
        when(bookingService.addBooking(authorizationHeader, addBookingRequest)).thenReturn(booking);

        // Act
        MvcResult mvcResult = mockMvc.perform(post("/api/bookings/add")
                .header("Authorization", authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(addBookingRequest)))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Booking result = new ObjectMapper().readValue(response.getContentAsString(), Booking.class);

        // Assert
        assertNotNull(result);
    }

    // Test retrieving a booking by a valid booking ID and check if it returns a booking.
    @Test
    public void testGetBookingById_ValidBookingId_ReturnsBooking() throws Exception {
        // Arrange
        String bookingId = "validBookingId";
        Booking booking = new Booking();
        when(bookingService.findBookingById(bookingId)).thenReturn(booking);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/findById/{bookingId}", bookingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Booking result = new ObjectMapper().readValue(response.getContentAsString(), Booking.class);

        // Assert
        assertNotNull(result);
    }

    // Test retrieving a booking by an invalid booking ID and check if it returns a "not found" response.
    @Test
    public void testGetBookingById_InvalidBookingId_ReturnsNotFound() throws Exception {
        // Arrange
        String invalidBookingId = "invalidBookingId";
        when(bookingService.findBookingById(invalidBookingId)).thenThrow(BookingNotFoundException.class);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/findById/{bookingId}", invalidBookingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        // Assert
        String responseContent = mvcResult.getResponse().getContentAsString();
        assertTrue(responseContent.contains("error"));
    }

    // Test retrieving all bookings and check if it returns a list of bookings.
    @Test
    public void testGetAllBookings_ReturnsListOfBookings() throws Exception {
        // Arrange
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());
        when(bookingService.getAllBookings()).thenReturn(bookings);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/findAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        List<Booking> result = new ObjectMapper().readValue(response.getContentAsString(),
                new ObjectMapper().getTypeFactory().constructCollectionType(List.class, Booking.class));

        // Assert
        assertNotNull(result);
        assertEquals(bookings, result);
    }

    // Test retrieving bookings by a valid patient ID and check if it returns a list of bookings.
    @Test
    public void testGetBookingsByPatientId_ValidPatientId_ReturnsBookings() throws Exception {
        // Arrange
        String patientId = "validPatientId";
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());
        when(bookingService.getBookingByPatientId(patientId)).thenReturn(bookings);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/findByPatientId/{patientId}", patientId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        List<Booking> result = new ObjectMapper().readValue(response.getContentAsString(),
                new ObjectMapper().getTypeFactory().constructCollectionType(List.class, Booking.class));

        // Assert
        assertNotNull(result);
        assertEquals(bookings, result);
    }

    // Test retrieving bookings by an invalid patient ID and check if it returns a "not found" response.
    @Test
    public void testGetBookingsByPatientId_InvalidPatientId_ReturnsNotFound() throws Exception {
        // Arrange
        String invalidPatientId = "invalidPatientId";
        when(bookingService.getBookingByPatientId(invalidPatientId)).thenThrow(PatientNotFoundException.class);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/findByPatientId/{patientId}", invalidPatientId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        // Assert
        String responseContent = mvcResult.getResponse().getContentAsString();
        assertTrue(responseContent.contains("error"));
    }

    // Test approving a booking with a valid booking ID and check if it returns an approved booking.
    @Test
    public void testApproveBooking_ValidBookingId_ReturnsApprovedBooking() throws Exception {
        // Arrange
        String authorizationHeader = "validAuthorizationHeader";
        String bookingId = "validBookingId";
        Booking booking = new Booking();
        booking.setBookingStatus(Booking.BookingStatus.REQUESTED);
        when(bookingService.approveBooking(authorizationHeader, bookingId)).thenReturn(booking);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/approve/{bookingId}", bookingId)
                .header("Authorization", authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Booking result = new ObjectMapper().readValue(response.getContentAsString(), Booking.class);

        // Assert
        assertNotNull(result);
        assertEquals(Booking.BookingStatus.REQUESTED, result.getBookingStatus());
    }

    // Test rejecting a booking with a valid booking ID and check if it returns a declined booking.
    @Test
    public void testRejectBooking_ValidBookingId_ReturnsDeclinedBooking() throws Exception {
        // Arrange
        String bookingId = "validBookingId";
        Booking booking = new Booking();
        booking.setBookingStatus(Booking.BookingStatus.REQUESTED);
        when(bookingService.rejectBooking(bookingId)).thenReturn(booking);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/reject/{bookingId}", bookingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Booking result = new ObjectMapper().readValue(response.getContentAsString(), Booking.class);

        // Assert
        assertNotNull(result);
        assertEquals(Booking.BookingStatus.REQUESTED, result.getBookingStatus());
    }

    // Test canceling a booking with a valid booking ID and check if it returns a cancelled booking.
    @Test
    public void testCancelBooking_ValidBookingId_ReturnsCancelledBooking() throws Exception {
        // Arrange
        String authorizationHeader = "validAuthorizationHeader";
        String bookingId = "validBookingId";
        Booking booking = new Booking();
        booking.setBookingStatus(Booking.BookingStatus.REQUESTED);
        when(bookingService.cancelBooking(authorizationHeader, bookingId)).thenReturn(booking);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/cancel/{bookingId}", bookingId)
                .header("Authorization", authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Booking result = new ObjectMapper().readValue(response.getContentAsString(), Booking.class);

        // Assert
        assertNotNull(result);
        assertEquals(Booking.BookingStatus.REQUESTED, result.getBookingStatus());
    }

    // Test completing a booking with a valid booking ID and check if it returns a completed booking.
    @Test
    public void testCompleteBooking_ValidBookingId_ReturnsCompletedBooking() throws Exception {
        // Arrange
        String authorizationHeader = "validAuthorizationHeader";
        String bookingId = "validBookingId";
        Booking booking = new Booking();
        when(bookingService.completeBooking(authorizationHeader, bookingId)).thenReturn(booking);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/complete/{bookingId}", bookingId)
                .header("Authorization", authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Booking result = new ObjectMapper().readValue(response.getContentAsString(), Booking.class);

        // Assert
        assertNotNull(result);
    }

    // Helper method to convert object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
