package com.Capg.Billing.Controller;

import com.Capg.Billing.Constants.PaymentStatus;
import com.Capg.Billing.DTO.BillingRequest;
import com.Capg.Billing.DTO.PaymentDetails;
import com.Capg.Billing.Exception.BillingNotFoundException;
import com.Capg.Billing.Model.Billing;
import com.Capg.Billing.Service.BillingService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Billing")
public class BillingController {
    @Autowired
    private BillingService billingService;

    @Value("${rzp.key.id}")
    private String KEY_ID;

    @Value("${rzp.key.secret}")
    private String KEY_SECRET;

    @Value("${rzp.currency}")
    private String CURRENCY;

    @Value("${rzp.company.name}")
    private String COMPANY;

    //Add billing details into the database
    @PostMapping("/addBilling")
    public Billing addBilling(@RequestHeader("Authorization") String authorizationHeader, @RequestBody BillingRequest billingRequest) throws BillingNotFoundException {
        return billingService.addBilling(authorizationHeader, billingRequest.getBookingId());
    }

    //Get billing details using billing ID, returns the billing object
    @GetMapping("/findBillingById/{billingId}")
    public Billing getBillingById(@PathVariable String billingId) throws BillingNotFoundException {
        return billingService.findById(billingId);
    }

    //Get billing details using booking ID, returns the billing object
    @GetMapping("/findByBookingId/{bookingId}")
    public Billing getByBookingId(@PathVariable String bookingId) {
        return billingService.findByBookingId(bookingId);
    }


    //Pay bill by redirecting to razorpay portal
    @GetMapping("/pay/{billingId}")
    public PaymentDetails payment(@PathVariable String billingId) throws RazorpayException, RuntimeException {
        try {
            Billing billing = billingService.findById(billingId);
            if (billing.getPaymentStatus() == PaymentStatus.COMPLETED) {
                throw new RuntimeException("Billing status is already COMPLETED");
            }

            RazorpayClient razorpay = new RazorpayClient(KEY_ID, KEY_SECRET);
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (billing.getBillAmount())*100);
            orderRequest.put("currency", CURRENCY);
            orderRequest.put("receipt", billing.getBillId());

            Order order = razorpay.orders.create(orderRequest);

            PaymentDetails paymentDetails = new PaymentDetails();
            paymentDetails.setKeyId(KEY_ID);
            paymentDetails.setId(order.get("id"));
            paymentDetails.setAmount(order.get("amount"));
            paymentDetails.setCurrency(order.get("currency"));
            paymentDetails.setBillingId(order.get("receipt"));

            return paymentDetails;
        } catch (RazorpayException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/pay/success/{billingId}")
    public Billing successPay(@PathVariable String billingId) throws BillingNotFoundException{
        return billingService.paymentSuccessfull(billingId);
    }
}
