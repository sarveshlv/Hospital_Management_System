package com.Capg.Patient.Constants;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Address {

    @NotBlank(message = "Street Address is required!")
    private String streetAddress;
    @NotBlank(message = "City name is required!")
    private String cityName;
    @NotBlank(message = "State name is required!")
    private String stateName;
}
