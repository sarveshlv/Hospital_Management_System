package com.Capg.Billing.BillingServiceTest;

import com.Capg.BedModule.Constants.BedType;
import com.Capg.Billing.Constants.PaymentStatus;
import com.Capg.Billing.DTO.Bed;
import com.Capg.Billing.DTO.Booking;
import com.Capg.Billing.Exception.BookingNotFoundException;
import com.Capg.Billing.Model.Billing;
import com.Capg.Billing.Repository.BillingRepository;
import com.Capg.Billing.Service.BillingServiceImpl;
import com.Capg.Billing.ServiceClients.BedServiceClient;
import com.Capg.Billing.ServiceClients.BookingServiceClient;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BillingServiceTest {

    @InjectMocks
    private BillingServiceImpl billingService;

    @Mock
    private BillingRepository billingRepository;

    @Mock
    private BookingServiceClient bookingServiceClient;

    @Mock
    private BedServiceClient bedServiceClient;

    @Test
    public void testAddBilling() throws BookingNotFoundException, ParseException {

        Booking booking = createBooking();

        Bed bed = new Bed();
        bed.setCostPerDay(50.0);

        Billing billing = createBilling();

        when(bookingServiceClient.completeBooking(anyString(), anyString())).thenReturn(booking);
        when(bedServiceClient.makeBedAvailable(anyString(), anyString())).thenReturn(bed);
        when(billingRepository.save(any(Billing.class))).thenReturn(billing);

        Billing result = billingService.addBilling("authorization", "book123");

        assertNotNull(result);
    }

    @Test
    public void testFindByBookingId() {

        Billing billing = createBilling();
        when(billingRepository.findByBookingId(anyString())).thenReturn(billing);
        Billing result = billingService.findByBookingId("book123");
        assertNotNull(result);
    }

    private static Billing createBilling() {
        Billing obj = new Billing();
        obj.setBillId("Bill123");
        obj.setBookingId("book123");
        obj.setPaymentStatus(PaymentStatus.COMPLETED);
        obj.setBillAmount(1000.0);
        return obj;
    }

    private static Booking createBooking() throws ParseException {
        Booking obj = new Booking();
        obj.setBookingId("book123");
        obj.setPatientId("pat123");
        obj.setHospitalId("abcd1234");
        obj.setBedId("bed123");
        obj.setBedType(String.valueOf(BedType.REGULAR_BED));
        obj.setBookingDate(getDate("2023-09-12"));
        obj.setFromDate(getDate("2023-09-14"));
        obj.setToDate(getDate("2023-09-16"));
        obj.setBookingStatus(String.valueOf(Booking.BookingStatus.REQUESTED));
        return obj;
    }

    private static Date getDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = format.parse(date);
        return date1;
    }
}
