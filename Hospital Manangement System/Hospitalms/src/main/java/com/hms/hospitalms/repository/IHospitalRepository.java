package com.hms.hospitalms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hms.hospitalms.entities.Hospital;

public interface IHospitalRepository extends MongoRepository<Hospital, String> {

	List<Hospital> findByAddressPincodeBetween(Long startPincode, Long endPincode);

}