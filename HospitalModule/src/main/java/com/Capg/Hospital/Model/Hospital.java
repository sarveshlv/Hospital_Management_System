package com.Capg.Hospital.Model;

import com.Capg.Hospital.Constants.HospitalType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "hospitals")
public class Hospital {

    @Id
    private String hospitalId;
    private String hospitalName;
    private String hospitalAddress;
    private Long pincode;
    private HospitalType hospitalType;
    private String status;

}
