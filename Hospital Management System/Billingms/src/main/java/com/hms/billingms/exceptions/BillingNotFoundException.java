package com.hms.billingms.exceptions;

public class BillingNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5145174606922710398L;

	public BillingNotFoundException(String billingId) {
		super("Billing not found for id: " + billingId);
	}	
}