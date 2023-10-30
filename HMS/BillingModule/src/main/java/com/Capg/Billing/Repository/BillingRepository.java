package com.Capg.Billing.Repository;

import com.Capg.Billing.Model.Billing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends MongoRepository<Billing, String> {
    Billing findByBookingId(String bookingId);
}
