package com.Capg.Hospital.DTO;

import com.Capg.Hospital.Constants.Address;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddHospitalDTO {

    @NotEmpty(message = "Hospital Name is required!")
    @Size(min = 3, max = 20, message = "Name should consist atleast 3 or upto 20 characters!")
    private String hospitalName;

    @NotEmpty(message = "Address is required!")
    private Address hospitalAddress;

    @NotEmpty(message = "Pincode is required!")
    @Pattern(regexp = "\\d{6}", message = "Pincode must be a 6 digit number!")
    private Long pincode;

    @NotNull(message = "Contact number is required")
    @Min(value = 6000000000L, message = "Invalid phone number")
    @Max(value = 9999999999L, message = "Invalid phone number")
    private Long contactNo;
    private String hospitalType;
}
