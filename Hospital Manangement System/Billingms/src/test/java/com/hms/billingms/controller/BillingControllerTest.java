package com.hms.billingms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.billingms.dto.BillingRequest;
import com.hms.billingms.entities.Billing;
import com.hms.billingms.service.IBillingService;
import com.hms.billingms.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;

public class BillingControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private BillingController billingController;

	@Mock
	private IBillingService billingService;

	@Mock
	private PaypalService paypalService;

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

	@Test
	public void testPayment_ValidBillingId_ReturnsApprovalUrl() throws Exception {
		// Arrange
		String billingId = "validBillingId";

		// Mock
		Billing billing = new Billing();
		billing.setId("1");
		billing.setPaymentStatus(Billing.PaymentStatus.PENDING);
		
		Payment payment = new Payment();
		List<Links> linksList = new ArrayList<>();
		linksList.add(new Links("approval_url", "http://example.com/approval"));
		when(billingService.findById(billingId)).thenReturn(billing);
		when(paypalService.createPayment(billing.getBillAmount(), "USD", "paypal", "sale", "Booking Charges",
				"http://localhost:8500/api/billings/pay/success/" + billingId,
				"http://localhost:8500/api/billings/pay/cancel/" + billingId)).thenReturn(payment);

//		MockHttpServletRequest request = new MockHttpServletRequest();
//		MockHttpServletResponse response = new MockHttpServletResponse();
//		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));

		// Act
		String approvalUrl = billingController.payment(billingId);

		// Assert
		assertNotNull(approvalUrl);
		assertEquals("http://example.com/approval", approvalUrl);
	}

	@Test
	public void testCancelPay() {
		// Arrange and Act
		RuntimeException exception = assertThrows(RuntimeException.class, () -> billingController.cancelPay());

		// Assert
		assertEquals("Payment was canceled.", exception.getMessage());
	}

	@Test
	public void testSuccessPay_ValidParameters_ReturnsCompletedBilling() throws Exception {
		// Arrange
		String billingId = "validBillingId";
		String paymentId = "validPaymentId";
		String payerId = "validPayerId";
		Payment payment = new Payment();
		payment.setState("approved");
		Billing billing = new Billing();
		when(paypalService.executePayment(paymentId, payerId)).thenReturn(payment);
		when(billingService.findById(billingId)).thenReturn(billing);
		when(billingService.addBill(billing)).thenReturn(billing);

		// Act
		MvcResult mvcResult = mockMvc.perform(get("/api/billings/pay/success/{billingId}", billingId)
				.param("paymentId", paymentId).param("PayerID", payerId)).andExpect(status().isOk()).andReturn();

		MockHttpServletResponse response = mvcResult.getResponse();
		Billing result = new ObjectMapper().readValue(response.getContentAsString(), Billing.class);

		// Assert
		assertNotNull(result);
		assertEquals(Billing.PaymentStatus.COMPLETED, result.getPaymentStatus());
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