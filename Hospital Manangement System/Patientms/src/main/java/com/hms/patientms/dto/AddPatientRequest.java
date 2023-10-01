package com.hms.patientms.dto;

import com.hms.patientms.entities.Patient;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddPatientRequest {
    @NotBlank(message = "First Name is required")
    @Size(max = 14, message = "Name cannot exceed 255 characters")
	private String firstName;
    @NotBlank(message = "First Name is required")
    @Size(max = 14, message = "Name cannot exceed 255 characters")
	private String lastName;
    @NotNull(message = "Contact number is required")
    @Min(value = 6000000000L, message = "Invalid phone number")
    @Max(value = 9999999999L, message = "Invalid phone number")
    private Long contactNumber;
    @NotNull(message = "Aadhar number is required")
    @Min(value = 100000000000L, message = "Invalid aadhar number")
    @Max(value = 999999999999L, message = "Invalid aadhar number")
    private Long aadharNumber;
	@Valid
	@NotNull(message = "Address is required")
	private Patient.Address address;
}