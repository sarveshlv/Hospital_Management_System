package com.hms.hospitalms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hms.hospitalms.dto.AddHospitalRequest;
import com.hms.hospitalms.entities.Hospital;
import com.hms.hospitalms.entities.Hospital.HospitalType;
import com.hms.hospitalms.exception.HospitalNotFoundException;
import com.hms.hospitalms.repository.IHospitalRepository;

@Service
public class HospitalService implements IHospitalService {

	@Autowired
	private IHospitalRepository hospitalRepository;

	@Override
	public Hospital addHospital(AddHospitalRequest addHospitalRequest) {
		Hospital hospital = new Hospital();
		hospital.setName(addHospitalRequest.getName());
		hospital.setHospitalType(HospitalType.valueOf(addHospitalRequest.getHospitalType()));
		hospital.setAddress(addHospitalRequest.getAddress());
		hospital.setContactNo(addHospitalRequest.getContactNo());
		hospital.setVerified(false);
		hospital = hospitalRepository.save(hospital);

		return hospital;

	}

	@Override
	public Hospital updateHospital(String id, AddHospitalRequest addHospitalRequest) throws HospitalNotFoundException {
		Hospital hospital = findHospitalById(id);
		hospital.setName(addHospitalRequest.getName());
		hospital.setVerified(false);
		hospital.setHospitalType(HospitalType.valueOf(addHospitalRequest.getHospitalType()));
		hospital.setAddress(addHospitalRequest.getAddress());
		return hospitalRepository.save(hospital);
	}

	@Override
	public Hospital findHospitalById(String id) throws HospitalNotFoundException {
		return hospitalRepository.findById(id).orElseThrow(() -> new HospitalNotFoundException(id));
	}

	@Override
	public Boolean isHospitalVerified(String id) throws HospitalNotFoundException {
		Hospital hospital = findHospitalById(id);
		return hospital.getVerified();
	}

	@Override
	public Hospital verifyHospital(String id) throws HospitalNotFoundException {
		Hospital hospital = findHospitalById(id);
		hospital.setVerified(true);
		return hospitalRepository.save(hospital);
	}

	@Override
	public List<Hospital> getNearbyHospitals(Long pincode) {
		return hospitalRepository.findByAddressPincodeBetween(pincode - 2, pincode + 2);
	}
}
