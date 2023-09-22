package com.Capg.Hospital.Repository;

import com.Capg.Hospital.Model.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {

    @Query("{'pincode':{$gte: ?0, $lte: ?1}}")
    List<Hospital> getHospitalByPincode(Long startPincode, Long endPincode);
}
