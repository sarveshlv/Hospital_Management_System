package com.Capg.Billing.Service;

import com.Capg.Billing.Exception.BillingNotFoundException;
import com.Capg.Billing.Exception.BookingNotFoundException;
import com.Capg.Billing.Model.Billing;

public interface BillingService {
    Billing addBilling(String authorizationHeader, String bookingId) throws BookingNotFoundException;

    Billing findById(String billingId) throws BillingNotFoundException;

    Billing findByBookingId(String bookingId);

    Billing paymentSuccessfull(String billingId) throws BillingNotFoundException;
}
