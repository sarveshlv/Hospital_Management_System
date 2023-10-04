package com.hms.billingms.repository;

import com.hms.billingms.entities.Billing;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Optional;

@DataMongoTest
public class BillingRepositoryTest {

    @Mock
    private BillingRepository billingRepository;

    public BillingRepositoryTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByBookingId_ExistingBilling_ReturnsBilling() {
        // Arrange
        Billing billing = new Billing();
        billing.setId("1");
        billing.setBookingId("1");
        billing.setBillAmount(100.0);
        billing.setPaymentStatus(Billing.PaymentStatus.PENDING);

        when(billingRepository.findByBookingId("1")).thenReturn(Optional.of(billing));

        // Act
        Optional<Billing> foundBilling = billingRepository.findByBookingId("1");

        // Assert
        assertTrue(foundBilling.isPresent());
        assertEquals("1", foundBilling.get().getBookingId());

        // Verify that the repository method was called
        verify(billingRepository, times(1)).findByBookingId("1");
    }

    @Test
    public void testFindByBookingId_NonExistingBilling_ReturnsEmptyOptional() {
        // Arrange
        when(billingRepository.findByBookingId("1")).thenReturn(Optional.empty());

        // Act
        Optional<Billing> foundBilling = billingRepository.findByBookingId("1");

        // Assert
        assertTrue(foundBilling.isEmpty());

        // Verify that the repository method was called
        verify(billingRepository, times(1)).findByBookingId("1");
    }
}