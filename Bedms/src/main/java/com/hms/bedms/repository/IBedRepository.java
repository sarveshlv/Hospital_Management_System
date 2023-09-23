package com.hms.bedms.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hms.bedms.entities.Bed;

public interface IBedRepository extends MongoRepository<Bed, String> {
	List<Bed> findByHospitalId(String hospitalId);

	List<Bed> findByBedType(String bedType);
}
