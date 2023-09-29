package com.hms.bedms.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBedRequest {
	@NotBlank(message = "Bed type is required")
	@Pattern(regexp = "USUAL_BED|ICU_BED|OXYGEN_BED|VENTILATOR_BED", message = "Invalid Bed type")
	private String bedType;
	@Positive(message = "Cost per day must be a positive number")
	private Double costPerDay;
}