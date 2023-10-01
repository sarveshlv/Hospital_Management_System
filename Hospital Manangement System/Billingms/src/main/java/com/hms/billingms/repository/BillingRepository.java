package com.hms.billingms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hms.billingms.entities.Billing;

public interface BillingRepository extends MongoRepository<Billing, String>{
    Billing findByBookingId(String bookingId);
}
