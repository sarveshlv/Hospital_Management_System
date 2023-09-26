package com.Capg.Billing.Service;

import com.Capg.Billing.Model.Billing;

public interface BillingService {
    Billing addBilling(String bookingId) throws BookingNotFoundException;

    Billing findById(String billingId) throws BillingNotFoundException;
}
