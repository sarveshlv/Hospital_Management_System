package com.Capg.Patient.Model;

import com.Capg.Patient.Constants.Address;
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
    private Long pincode;

}
