package com.hms.billingms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {
	private String id;
	private String keyId;
	private String billingId;
	private Integer amount;
	private String currency;
}