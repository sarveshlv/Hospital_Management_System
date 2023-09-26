package com.Capg.Billing.Controller;

import com.Capg.Billing.Exception.BillingNotFoundException;
import com.Capg.Billing.Model.Billing;
import com.Capg.Billing.Service.BillingService;
import com.Capg.Billing.Service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Billing")
public class BillingController {
    @Autowired
    private BillingService billingService;
    @Autowired
    private PaypalService paypalService;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

    @PostMapping("/addBilling/{bookingId}")
    public Billing addBilling(@PathVariable String bookingId) throws BillingNotFoundException {
        return billingService.addBilling(bookingId);
    }

    @GetMapping("/findBillingById/{billingId}")
    public Billing getBillingById(@PathVariable String billingId) throws BillingNotFoundException {
        return billingService.findById(billingId);
    }

    @PostMapping("/pay/{billingId}")
    public String payment(@PathVariable String billingId) throws PayPalRESTException {
        Billing billing = billingService.findById(billingId);
        Payment payment = paypalService.createPayment(billing.getBillAmount(), "INR", "paypal", "sale", "Booking Charges",
                "http://localhost:9090/Billing" + SUCCESS_URL, "http://localhost:9090/Billing" + CANCEL_URL);

        for (Links link : payment.getLinks()) {
            if (link.getRel().equals("approval_url")) {
                return "redirect:" + link.getHref();
            }
        }
        return "redirect:/";
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "failed";
    }
}
