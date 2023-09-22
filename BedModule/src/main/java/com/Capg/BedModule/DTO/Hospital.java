package com.Capg.BedModule.DTO;

import lombok.Data;

@Data
public class Hospital {

    private String hospitalId;
    private String hospitalName;
    private String hospitalAddress;
    private Long pincode;
    private String hospitalType;
    private String status;
}
