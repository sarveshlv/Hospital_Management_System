package com.Capg.Booking.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
public class AddBookingDTO {

    @NotBlank(message = "Patient ID is required")
    private String patientId;
    @NotBlank(message = "Hospital ID is required")
    private String hospitalId;
    @NotBlank(message = "Bed type is required")
    @Pattern(regexp = "REGULAR_BED|ICU_BED|OXYGEN_BED|VENTILATOR_BED", message = "Invalid Bed type")
    private String bedType;
    @NotNull(message = "Occupying/From date is required")
    @Future(message = "Occupying/From date must be in the future")
    private Date fromDate;
    @NotNull(message = "Release/To date is required")
    @Future(message = "Release/To date must be in the future")
    private Date toDate;
}
