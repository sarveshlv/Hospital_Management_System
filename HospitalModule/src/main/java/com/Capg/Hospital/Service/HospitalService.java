package com.Capg.Hospital.Service;

import com.Capg.Hospital.DTO.AddHospitalDTO;
import com.Capg.Hospital.Model.Hospital;

import java.util.List;

public interface HospitalService {
    Hospital registerHospital(AddHospitalDTO hospitalDTO);

    Hospital verifyHospital(String hospitalId, String status);

    List<Hospital> findNearbyHospitals(Long pincode);

    Hospital findHospitalById(String hospitalId);

    Hospital deleteHospitalById(String hospitalId);
}
