package com.Capg.Patient.Model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "patients")
public class Patient {
    @Id
    private String patientId;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private Long contactNumber;
    @Indexed(unique = true)
    private Long aadharCard;
    private Address address;

    @Data
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

}