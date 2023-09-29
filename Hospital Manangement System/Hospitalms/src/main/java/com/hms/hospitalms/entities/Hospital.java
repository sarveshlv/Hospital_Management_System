package com.hms.hospitalms.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Document(collection = "hospitals")
@Data
public class Hospital {
	@Id
	private String id;
	private String name;
	private Boolean verified;
	@Field
	private HospitalType hospitalType;
	private Long contactNo;
	private Address address;

	@Data
	@AllArgsConstructor
	public static class Address {
		@NotBlank(message = "City is required")
		private String city;
		@NotBlank(message = "State is required")
		private String state;
		@NotNull(message = "Pincode is required")
		@Min(value = 100000L, message = "Pincode must be a 6-digit number")
	    @Max(value = 999999L, message = "Pincode must be a 6-digit number")
		private Long pincode;
	}

	public enum HospitalType {
		GOVT, PRIVATE, SEMI_PRIVATE
	}
}