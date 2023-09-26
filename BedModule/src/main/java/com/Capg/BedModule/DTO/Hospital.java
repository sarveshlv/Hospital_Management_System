package com.Capg.BedModule.DTO;

import com.Capg.BedModule.Constants.Address;
import com.Capg.BedModule.Constants.HospitalType;
import lombok.Data;

@Data
public class Hospital {

    private String hospitalId;
    private String hospitalName;
    private Address hospitalAddress;
    private Long pincode;
    private HospitalType hospitalType;
    private String status;
}
