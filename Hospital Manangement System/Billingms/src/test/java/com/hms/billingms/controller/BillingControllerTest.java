package com.hms.billingms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.billingms.dto.BillingRequest;
import com.hms.billingms.entities.Billing;
import com.hms.billingms.service.IBillingService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BillingControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private BillingController billingController;

	@Mock
	private IBillingService billingService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(billingController).build();
	}

	@Test
	public void testAddBilling_ValidBillingRequest_ReturnsBilling() throws Exception {
		// Arrange
		String authorizationHeader = "validAuthorizationHeader";
		BillingRequest billingRequest = new BillingRequest();
		billingRequest.setBookingId("validBookingId");
		Billing billing = new Billing();
		when(billingService.generateBill(authorizationHeader, "validBookingId")).thenReturn(billing);

		// Act
		MvcResult mvcResult = mockMvc
				.perform(post("/api/billings/add").header("Authorization", authorizationHeader)
						.contentType(MediaType.APPLICATION_JSON).content(asJsonString(billingRequest)))
				.andExpect(status().isOk()).andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		Billing result = new ObjectMapper().readValue(response.getContentAsString(), Billing.class);

		// Assert
		assertNotNull(result);
	}

	@Test
	public void testGetBillingById_ValidBillingId_ReturnsBilling() throws Exception {
		// Arrange
		String billingId = "validBillingId";
		Billing billing = new Billing();
		when(billingService.findById(billingId)).thenReturn(billing);

		// Act
		MvcResult mvcResult = mockMvc
				.perform(get("/api/billings/findById/{billingId}", billingId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		Billing result = new ObjectMapper().readValue(response.getContentAsString(), Billing.class);

		// Assert
		assertNotNull(result);
	}

	@Test
	public void testGetBillingByBookingId_ValidBookingId_ReturnsBilling() throws Exception {
		// Arrange
		String bookingId = "validBookingId";
		Billing billing = new Billing();
		when(billingService.findByBookingId(bookingId)).thenReturn(billing);

		// Act
		MvcResult mvcResult = mockMvc.perform(
				get("/api/billings/findByBookingId/{bookingId}", bookingId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		Billing result = new ObjectMapper().readValue(response.getContentAsString(), Billing.class);

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