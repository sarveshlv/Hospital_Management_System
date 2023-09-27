package com.hms.billingms.dto;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Bed {
	private String id;
	private String hospitalId;
	private String bedType;
	private String bedStatus;
	private Double costPerDay;
}