package com.Capg.Hospital.Service;

import com.Capg.Hospital.DTO.AddHospitalDTO;
import com.Capg.Hospital.DTO.UpdateHospitalDTO;
import com.Capg.Hospital.Exceptions.HospitalNotFoundException;
import com.Capg.Hospital.Model.Hospital;

import java.util.List;

public interface HospitalService {
    Hospital registerHospital(AddHospitalDTO hospitalDTO);

    List<Hospital> getAllHospitals();

    Hospital updateHospital(String id, UpdateHospitalDTO updateHospitalDTO) throws HospitalNotFoundException;

    Hospital verifyHospital(String hospitalId, String status);

    List<Hospital> findNearbyHospitals(Long pincode);

    Hospital findHospitalById(String hospitalId);

    Hospital deleteHospitalById(String hospitalId);
}
