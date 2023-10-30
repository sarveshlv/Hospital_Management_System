package com.Capg.Billing.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class Booking {

    private String bookingId;
    private String patientId;
    private String hospitalId;
    private String bedId;
    private String bedType;
    private Date bookingDate;
    private Date fromDate;
    private Date toDate;
    private String bookingStatus;

    public enum BookingStatus {
        REQUESTED, APPROVED, DECLINED, CANCELLED, COMPLETED
    }
}
