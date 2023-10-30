package com.Capg.Billing.DTO;

import lombok.Data;

@Data
public class Bed {

    private String bedId;
    private String hospitalId;
    private String bedType;
    private String bedStatus;
    private Double costPerDay;
}
