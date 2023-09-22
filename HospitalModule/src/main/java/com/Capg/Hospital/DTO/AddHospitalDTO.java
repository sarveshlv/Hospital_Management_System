package com.Capg.Hospital.DTO;

import com.Capg.Hospital.Constants.HospitalType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddHospitalDTO {

    @NotEmpty(message = "Hospital Name is required!")
    @Size(min = 3, max = 20, message = "Name should consist atleast 3 or upto 20 characters!")
    private String hospitalName;
    @NotEmpty(message = "Address is required!")
    @Size(min = 10, message = "Address should be atleast more than 10 characters!")
    private String hospitalAddress;
    @NotEmpty(message = "Pincode is required!")
    @Pattern(regexp = "\\d{6}", message = "Pincode must be a 6 digit number!")
    private Long pincode;
    private String hospitalType;
}
