package com.Capg.Booking.Model;

import com.Capg.Booking.Constants.BedType;
import com.Capg.Booking.Constants.BookingStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "bookings")
public class Booking {

    @Id
    private String bookingId;
    private String patientId;
    private String hospitalId;
    private String bedId;
    private BedType bedType;
    private Date bookingDate;
    private Date fromDate;
    private Date toDate;
    private BookingStatus bookingStatus;

}
