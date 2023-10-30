package com.Capg.Booking.BookingControllerTest;

import com.Capg.Booking.Constants.BedType;
import com.Capg.Booking.Constants.BookingStatus;
import com.Capg.Booking.Controller.BookingController;
import com.Capg.Booking.DTO.AddBookingDTO;
import com.Capg.Booking.Exception.CentralExceptionHandler;
import com.Capg.Booking.Model.Booking;
import com.Capg.Booking.Service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).setControllerAdvice(new CentralExceptionHandler())
                .build();
    }

    @Test
    public void testAddBooking() throws Exception {
        String authorizationHeader = "Bearer yourAccessToken";
        AddBookingDTO addBookingDTO = createAddBookingDTO();
        Booking booking = createBooking();

        when(bookingService.addBooking(eq(authorizationHeader), eq(addBookingDTO))).thenReturn(booking);

        mockMvc.perform(post("/Booking/addBooking")
                        .header("Authorization", authorizationHeader)
                        .contentType(APPLICATION_JSON)
                        .content(asJsonString(addBookingDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.bookingId").value(booking.getBookingId()));

        verify(bookingService, times(1)).addBooking(eq(authorizationHeader), eq(addBookingDTO));
    }

    @Test
    public void testGetBookingById() throws Exception {
        String bookingId = "book123";
        Booking booking = createBooking();

        when(bookingService.findBookingById(eq(bookingId))).thenReturn(booking);

        mockMvc.perform(get("/Booking/findBooking/{bookingId}", bookingId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.bookingId").value(booking.getBookingId()));

        verify(bookingService, times(1)).findBookingById(eq(bookingId));
    }

    @Test
    public void testGetBookingsByPatientId() throws Exception {
        String patientId = "pat123";
        List<Booking> bookings = new ArrayList<>();
        Booking booking = createBooking();
        bookings.add(booking);

        when(bookingService.getBookingByPatientId(eq(patientId))).thenReturn(bookings);

        mockMvc.perform(get("/Booking/findByPatientId/{patientId}", patientId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(bookingService, times(1)).getBookingByPatientId(eq(patientId));
    }

    @Test
    public void testGetBookingByHospitalId() throws Exception {
        String hospitalId = "abcd1234";
        List<Booking> bookings = new ArrayList<>();
        Booking booking = createBooking();
        bookings.add(booking);

        when(bookingService.getBookingByHospitalId(eq(hospitalId))).thenReturn(bookings);

        mockMvc.perform(get("/Booking/findByHospitalId/{hospitalId}", hospitalId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(bookingService, times(1)).getBookingByHospitalId(eq(hospitalId));
    }

    @Test
    public void testCancelBooking() throws Exception {
        String authorizationHeader = "Bearer yourAccessToken";
        String bookingId = "book123";
        Booking booking = createBooking();

        when(bookingService.cancelBooking(eq(authorizationHeader), eq(bookingId))).thenReturn(booking);

        mockMvc.perform(put("/Booking/cancelBooking/{bookingId}", bookingId)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.bookingId").value(booking.getBookingId()));

        verify(bookingService, times(1)).cancelBooking(eq(authorizationHeader), eq(bookingId));
    }

    @Test
    public void testApproveBooking() throws Exception {
        String bookingId = "book123";
        Booking booking = createBooking();

        when(bookingService.approveBooking(eq(bookingId))).thenReturn(booking);

        mockMvc.perform(put("/Booking/approveBooking/{bookingId}", bookingId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.bookingId").value(booking.getBookingId()));

        verify(bookingService, times(1)).approveBooking(eq(bookingId));
    }

    @Test
    public void testDeclineBooking() throws Exception {
        String authorizationHeader = "Bearer yourAccessToken";
        String bookingId = "book123";
        Booking booking = createBooking();

        when(bookingService.declineBooking(eq(authorizationHeader), eq(bookingId))).thenReturn(booking);

        mockMvc.perform(put("/Booking/declineBooking/{bookingId}", bookingId)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.bookingId").value(booking.getBookingId()));

        verify(bookingService, times(1)).declineBooking(eq(authorizationHeader), eq(bookingId));
    }

    @Test
    public void testCompleteBooking() throws Exception {
        String bookingId = "book123";
        Booking booking = createBooking();

        when(bookingService.completeBooking(eq(bookingId))).thenReturn(booking);

        mockMvc.perform(put("/Booking/completeBooking/{bookingId}", bookingId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.bookingId").value(booking.getBookingId()));

        verify(bookingService, times(1)).completeBooking(eq(bookingId));
    }

    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
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

    private static AddBookingDTO createAddBookingDTO() {
        AddBookingDTO obj = new AddBookingDTO();
        obj.setPatientId("pat123");
        obj.setHospitalId("abcd1234");
        obj.setBedType(String.valueOf(BedType.REGULAR_BED));
        obj.setFromDate("2023-09-14");
        obj.setToDate("2023-09-16");
        return obj;
    }

    private static Date getDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = format.parse(date);
        return date1;
    }
}
