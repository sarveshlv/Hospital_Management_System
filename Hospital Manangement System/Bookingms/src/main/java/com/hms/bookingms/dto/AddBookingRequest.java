package com.hms.bookingms.dto;

import java.util.Date;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddBookingRequest {
	@NotBlank(message = "Patient ID is required")
	private String patientId;
	@NotBlank(message = "Hospital ID is required")
	private String hospitalId;
	@NotBlank(message = "Bed type is required")
    @Pattern(regexp = "USUAL_BED|ICU_BED|OXYGEN_BED|VENTILATOR_BED", message = "Invalid Bed type")
    private String bedType;
	@NotNull(message = "Occupy date is required")
	@Future(message = "Occupy date must be in the future")
	private Date occupyDate;
	@NotNull(message = "Release date is required")
	@Future(message = "Release date must be in the future")
	private Date releaseDate;
}
