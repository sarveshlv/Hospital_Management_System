package com.hms.billingms.controller;

import com.hms.billingms.dto.BillingRequest;
import com.hms.billingms.entities.Billing;
import com.hms.billingms.exceptions.BillingNotFoundException;
import com.hms.billingms.service.IBillingService;
import com.hms.billingms.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billings")
@Slf4j
public class BillingController {

	@Autowired
	private IBillingService billingService;

	@Autowired
	private PaypalService paypalService;

	public static final String SUCCESS_URL = "/pay/success";
	public static final String CANCEL_URL = "/pay/cancel";

	@PostMapping("/add")
	public Billing addBilling(@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody BillingRequest billingRequest) throws BillingNotFoundException {
		return billingService.addBilling(authorizationHeader, billingRequest.getBookingId());
	}

	@GetMapping("/findById/{billingId}")
	public Billing getBillingById(@PathVariable String billingId) throws BillingNotFoundException {
		return billingService.findById(billingId);
	}
	
	@GetMapping("/findByBookingId/{bookingId}")
	public Billing getBillingByBookingId(@PathVariable String bookingId){
		return billingService.findByBillingId(bookingId);
	}

	@GetMapping("/pay/{billingId}")
	public String payment(@PathVariable String billingId) throws PayPalRESTException {
		Billing billing = billingService.findById(billingId);
		if (billing.getPaymentStatus() == Billing.PaymentStatus.COMPLETED) {
			throw new RuntimeException();
		}
		Payment payment = paypalService.createPayment(billing.getBillAmount(), "USD", "paypal", "sale",
				"Booking Charges", "http://localhost:8500/api/billings" + SUCCESS_URL,
				"http://localhost:8500/api/billings" + CANCEL_URL);

		log.info("Links: {}", payment.getLinks().toString());
		for (Links link : payment.getLinks()) {
			if (link.getRel().equals("approval_url")) {
				return link.getHref();
			}
		}
		return "";
	}

	@GetMapping(value = CANCEL_URL)
	public String cancelPay() {
		throw new RuntimeException("Payment was canceled.");
	}

	@GetMapping(value = SUCCESS_URL)
	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) throws PayPalRESTException {
		Payment payment = paypalService.executePayment(paymentId, payerId);
		log.info("Payment details: {}", payment.toJSON());
		if (payment.getState().equals("approved")) {
			return "success";
		} else {
			throw new RuntimeException("Payment was not approved.");
		}

	}
}