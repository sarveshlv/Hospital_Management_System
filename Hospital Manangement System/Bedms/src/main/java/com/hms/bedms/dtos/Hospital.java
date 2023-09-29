package com.hms.bedms.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Hospital {
	private String id;
	private String name;
	private Boolean verified;
	private String hospitalType;
	private Long contactNo;
	private Address address;

	@Data
	@AllArgsConstructor
	public static class Address {
		private String city;
		private String state;
		private Long pincode;
	}

	public enum HospitalType {
		GOVT, PRIVATE, SEMI_PRIVATE
	}
}
