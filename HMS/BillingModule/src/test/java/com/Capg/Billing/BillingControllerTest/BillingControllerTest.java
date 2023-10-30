package com.Capg.Billing.BillingControllerTest;

import com.Capg.Billing.Constants.PaymentStatus;
import com.Capg.Billing.Controller.BillingController;
import com.Capg.Billing.DTO.BillingRequest;
import com.Capg.Billing.Exception.CentralExceptionHandler;
import com.Capg.Billing.Model.Billing;
import com.Capg.Billing.Service.BillingService;
import com.Capg.Billing.Service.PaypalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BillingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BillingService billingService;

    @Mock
    private PaypalService paypalService;

    @InjectMocks
    private BillingController billingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(billingController).setControllerAdvice(new CentralExceptionHandler())
                .build();
    }

    @Test
    public void testAddBilling() throws Exception {
        Billing billing = createBilling(); // Create a mock Billing object

        when(billingService.addBilling(anyString(), anyString())).thenReturn(billing);

        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setBookingId("book123");

        mockMvc.perform(post("/Billing/addBilling")
                        .header("Authorization", "Bearer yourAuthToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(billingRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.billId").value(billing.getBillId()));

        verify(billingService, times(1)).addBilling(anyString(), anyString());
    }

    @Test
    public void testGetBillingById() throws Exception {
        Billing billing = createBilling(); // Create a mock Billing object

        when(billingService.findById(anyString())).thenReturn(billing);

        mockMvc.perform(get("/Billing/findBillingById/{billingId}", "billing123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.billId").value(billing.getBillId()));

        verify(billingService, times(1)).findById(anyString());
    }

    @Test
    public void testGetByBookingId() throws Exception {
        Billing billing = createBilling(); // Create a mock Billing object

        when(billingService.findByBookingId(anyString())).thenReturn(billing);

        mockMvc.perform(get("/Billing/findByBookingId/{bookingId}", "booking123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.billId").value(billing.getBillId()));

        verify(billingService, times(1)).findByBookingId(anyString());
    }

//    @Test
//    public void testPayment() throws Exception {
//        // Create a mock Billing object
//        Billing billing = createBilling();
//
//        Payment payment = new Payment();
//        payment.setLinks(Collections.singletonList(new Links("approval_url", "http://approval.url")));
//
//        when(billingService.findById(anyString())).thenReturn(billing);
//        when(paypalService.createPayment(anyDouble(), anyString(), anyString(), anyString(), anyString(),
//                anyString(), anyString())).thenReturn(payment);
//
//        mockMvc.perform(get("/Billing/pay/{billingId}", "Bill123"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.ALL_VALUE))
//                .andExpect(content().string("http://approval.url"));
//
//        verify(billingService, times(1)).findById(anyString());
//        verify(paypalService, times(1)).createPayment(anyDouble(), anyString(), anyString(), anyString(),
//                anyString(), anyString(), anyString());
//    }

    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private static Billing createBilling() {
        Billing obj = new Billing();
        obj.setBillId("Bill123");
        obj.setBookingId("book123");
        obj.setPaymentStatus(PaymentStatus.COMPLETED);
        obj.setBillAmount(1000.0);
        return obj;
    }


}
