package com.Capg.Billing.DTO;

import lombok.Data;

@Data
public class PaymentDetails {
    private String id;
    private String keyId;
    private String billingId;
    private Integer amount;
    private String currency;
}
