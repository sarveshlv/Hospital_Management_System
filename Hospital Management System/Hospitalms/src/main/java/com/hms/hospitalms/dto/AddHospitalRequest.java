package com.hms.hospitalms.dto;

import com.hms.hospitalms.entities.Hospital;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddHospitalRequest {
	@NotBlank(message = "Name is required")
	@Size(max = 255, message = "Name cannot exceed 255 characters")
	private String name;

	@NotBlank(message = "Hospital type is required")
	@Pattern(regexp = "GOVT|PRIVATE|SEMI_PRIVATE", message = "Invalid hospital type")
	private String hospitalType;

	@NotNull(message = "Contact number is required")
	@Min(value = 6000000000L, message = "Invalid phone number")
	@Max(value = 9999999999L, message = "Invalid phone number")
	private Long contactNo;

	@NotNull(message = "Address is required")
	@Valid
	private Hospital.Address address;
}