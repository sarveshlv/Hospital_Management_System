package com.Capg.Billing.Service;

import com.Capg.Billing.Constants.PaymentStatus;
import com.Capg.Billing.DTO.Bed;
import com.Capg.Billing.DTO.Booking;
import com.Capg.Billing.Exception.BillingNotFoundException;
import com.Capg.Billing.Exception.BookingNotFoundException;
import com.Capg.Billing.Model.Billing;
import com.Capg.Billing.Repository.BillingRepository;
import com.Capg.Billing.ServiceClients.BedServiceClient;
import com.Capg.Billing.ServiceClients.BookingServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceImpl implements BillingService{

    @Autowired
    private BillingRepository billingRepository;
    @Autowired
    private BookingServiceClient bookingServiceClient;
    @Autowired
    private BedServiceClient bedServiceClient;

    @Override
    public Billing addBilling(String bookingId) throws BookingNotFoundException {
        Booking booking = bookingServiceClient.completeBooking(bookingId);
        Bed bed = bedServiceClient.makeBedAvailable(bookingId);

        Billing billing = new Billing();
        long differenceInMilliseconds = booking.getToDate().getTime() - booking.getFromDate().getTime();
        long differenceInDays = differenceInMilliseconds / (24 * 60 * 60 * 1000);

        billing.setBookingId(bookingId);
        billing.setBillAmount(100 + bed.getCostPerDay()*differenceInDays);
        billing.setPaymentStatus(PaymentStatus.PENDING);

        return billingRepository.save(billing);
    }

    @Override
    public Billing findById(String billingId) throws BillingNotFoundException {
        return billingRepository.findById(billingId).orElseThrow(() -> new BillingNotFoundException(billingId));
    }
}
