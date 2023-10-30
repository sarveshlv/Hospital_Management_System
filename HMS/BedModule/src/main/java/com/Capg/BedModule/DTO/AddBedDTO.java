package com.Capg.BedModule.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddBedDTO {

    //Bed DTO for retrieving only particular details of Bed object

    @NotBlank(message = "Hospital ID is required!")
    private String hospitalId;
    @NotBlank(message = "Bed type is required!")
    @Pattern(regexp = "REGULAR_BED|ICU_BED|OXYGEN_BED|VENTILATOR_BED", message = "Invalid Bed type!")
    private String bedType;
    @Positive(message = "Cost must be a positive number!")
    private Double costPerDay;
}
