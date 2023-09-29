package com.hms.bedms.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "beds")
public class Bed {
    @Id
    private String id;
    private String hospitalId;
    private BedType bedType;
    private BedStatus bedStatus;
    private Double costPerDay;
    public enum BedType {
        USUAL_BED, ICU_BED, OXYGEN_BED, VENTILATOR_BED
    }
    public enum BedStatus {
        AVAILABLE, BOOKED, CANCELLED, COMPLETED
    }
}