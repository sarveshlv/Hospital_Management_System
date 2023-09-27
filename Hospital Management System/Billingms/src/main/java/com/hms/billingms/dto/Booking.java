package com.hms.billingms.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
	private String id;
	private String patientId;
	private String hospitalId;
	private String bedId;
	private String bedType;
	private Date bookingDate;
	private Date occupyDate;
	private Date releaseDate;
	private String bookingStatus;
	
	public enum BookingStatus {
		REQUESTED, APPROVED, DECLINED, CANCELLED, COMPLETED
	}
}
