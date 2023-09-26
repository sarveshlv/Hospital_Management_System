package com.Capg.Patient.DTO;

import com.Capg.Patient.Constants.Address;
import com.Capg.Patient.Model.Patient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddPatientDTO {

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
    private Address address;
    @NotNull(message = "Pincode is required!")
    private Long pincode;
}
