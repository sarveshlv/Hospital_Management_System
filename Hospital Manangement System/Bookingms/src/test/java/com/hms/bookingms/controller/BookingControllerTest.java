package com.hms.bookingms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.bookingms.dto.AddBookingRequest;
import com.hms.bookingms.entities.Booking;
import com.hms.bookingms.exceptions.BookingNotFoundException;
import com.hms.bookingms.exceptions.InvalidBookingRequest;
import com.hms.bookingms.exceptions.InvalidDatesException;
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
import static org.mockito.ArgumentMatchers.*;
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

    @Test
    public void testAddBooking_ValidRequest_ReturnsBooking() throws Exception {
        // Arrange
        AddBookingRequest request = new AddBookingRequest("patientId", "hospitalId", "USUAL_BED",
                new Date(System.currentTimeMillis() + 3600000), new Date(System.currentTimeMillis() + 7200000));
        Booking booking = new Booking();
        when(bookingService.addBooking(any())).thenReturn(booking);

        // Act
        MvcResult mvcResult = mockMvc.perform(post("/api/bookings/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Booking result = new ObjectMapper().readValue(response.getContentAsString(), Booking.class);

        // Assert
        assertNotNull(result);
        assertEquals(booking, result);
    }

    @Test
    public void testAddBooking_InvalidRequest_ReturnsBadRequest() throws Exception {
        // Arrange
        AddBookingRequest request = new AddBookingRequest();
        when(bookingService.addBooking(any())).thenThrow(InvalidDatesException.class);

        // Act
        mockMvc.perform(post("/api/bookings/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testGetBookingById_ValidId_ReturnsBooking() throws Exception {
        // Arrange
        String bookingId = "validBookingId";
        Booking booking = new Booking();
        when(bookingService.findBookingById(bookingId)).thenReturn(booking);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/find/{bookingId}", bookingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Booking result = new ObjectMapper().readValue(response.getContentAsString(), Booking.class);

        // Assert
        assertNotNull(result);
        assertEquals(booking, result);
    }

    @Test
    public void testGetBookingById_InvalidId_ReturnsNotFound() throws Exception {
        // Arrange
        String invalidBookingId = "invalidBookingId";
        when(bookingService.findBookingById(invalidBookingId)).thenThrow(BookingNotFoundException.class);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/find/{bookingId}", invalidBookingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        // Assert
        String responseContent = mvcResult.getResponse().getContentAsString();
        assertTrue(responseContent.contains("error"));
    }

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

    @Test
    public void testApproveBooking_ValidBookingId_ReturnsApprovedBooking() throws Exception {
        // Arrange
        String bookingId = "validBookingId";
        Booking booking = new Booking();
        booking.setBookingStatus(Booking.BookingStatus.REQUESTED);
        when(bookingService.approveBooking(bookingId)).thenReturn(booking);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/approve/{bookingId}", bookingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Booking result = new ObjectMapper().readValue(response.getContentAsString(), Booking.class);

        // Assert
        assertNotNull(result);
        assertEquals(Booking.BookingStatus.REQUESTED, result.getBookingStatus());
    }

    @Test
    public void testApproveBooking_InvalidBookingStatus_ReturnsBadRequest() throws Exception {
        // Arrange
        String bookingId = "validBookingId";
        when(bookingService.approveBooking(bookingId)).thenThrow(InvalidBookingRequest.class);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/approve/{bookingId}", bookingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        String responseContent = mvcResult.getResponse().getContentAsString();
        assertTrue(responseContent.contains("error"));
    }

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

    @Test
    public void testRejectBooking_InvalidBookingStatus_ReturnsBadRequest() throws Exception {
        // Arrange
        String bookingId = "validBookingId";
        when(bookingService.rejectBooking(bookingId)).thenThrow(InvalidBookingRequest.class);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/reject/{bookingId}", bookingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        String responseContent = mvcResult.getResponse().getContentAsString();
        assertTrue(responseContent.contains("error"));
    }

    @Test
    public void testCancelBooking_ValidBookingId_ReturnsCancelledBooking() throws Exception {
        // Arrange
        String bookingId = "validBookingId";
        Booking booking = new Booking();
        booking.setBookingStatus(Booking.BookingStatus.APPROVED);
        when(bookingService.cancelBooking(bookingId)).thenReturn(booking);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/cancel/{bookingId}", bookingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        Booking result = new ObjectMapper().readValue(response.getContentAsString(), Booking.class);

        // Assert
        assertNotNull(result);
        assertEquals(Booking.BookingStatus.APPROVED, result.getBookingStatus());
    }

    @Test
    public void testCancelBooking_InvalidBookingStatus_ReturnsBadRequest() throws Exception {
        // Arrange
        String bookingId = "validBookingId";
        when(bookingService.cancelBooking(bookingId)).thenThrow(InvalidBookingRequest.class);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/api/bookings/cancel/{bookingId}", bookingId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        String responseContent = mvcResult.getResponse().getContentAsString();
        assertTrue(responseContent.contains("error"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
