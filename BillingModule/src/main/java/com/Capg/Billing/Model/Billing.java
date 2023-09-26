package com.Capg.Billing.Model;

import com.Capg.Billing.Constants.PaymentStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "billings")
public class Billing {
    @Id
    private String billId;
    private String bookingId;
    private Double billAmount;
    private PaymentStatus paymentStatus;
}
