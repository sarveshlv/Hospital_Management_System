package com.Capg.Billing.BillingRepositoryTest;

import com.Capg.Billing.Constants.PaymentStatus;
import com.Capg.Billing.Model.Billing;
import com.Capg.Billing.Repository.BillingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BillingRepositoryTest {

    @Mock
    private BillingRepository billingRepository;


    @Test
    public void testFindByBookingId() {
        // Create a mock Billing object
        Billing billing = createBilling();

        // Mock behavior for mongoTemplate.findOne
        when(billingRepository.findByBookingId(billing.getBookingId())).thenReturn(billing);

        // Call the findByBookingId method
        Billing result = billingRepository.findByBookingId("book123");

        // Assertions
        assertNotNull(result);
        assertEquals("Bill123", result.getBillId());
        assertEquals("book123", result.getBookingId());
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
