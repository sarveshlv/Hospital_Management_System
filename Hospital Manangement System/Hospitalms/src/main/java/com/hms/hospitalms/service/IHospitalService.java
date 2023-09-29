package com.hms.hospitalms.service;

import com.hms.hospitalms.dto.AddHospitalRequest;
import com.hms.hospitalms.entities.Hospital;
import com.hms.hospitalms.exception.HospitalNotFoundException;

import java.util.List;

public interface IHospitalService {
    
    Hospital addHospital(AddHospitalRequest addHospitalRequest);
    Hospital updateHospital(String id, AddHospitalRequest addHospitalRequest) throws HospitalNotFoundException;
    Hospital findHospitalById(String id) throws HospitalNotFoundException;
    Boolean isHospitalVerified(String id) throws HospitalNotFoundException;
    Hospital verifyHospital(String id) throws HospitalNotFoundException;
    List<Hospital> getNearbyHospitals(Long pincode);
	List<Hospital> getAllHositals();
}