package com.Capg.Hospital.DTO;

import com.Capg.Hospital.Constants.Address;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateHospitalDTO {

    @NotNull(message = "Contact number is required")
    @Min(value = 6000000000L, message = "Invalid phone number")
    @Max(value = 9999999999L, message = "Invalid phone number")
    private Long contactNo;

    @NotEmpty(message = "Address is required!")
    private Address hospitalAddress;

    @NotEmpty(message = "Pincode is required!")
    @Pattern(regexp = "\\d{6}", message = "Pincode must be a 6 digit number!")
    private Long pincode;
}
