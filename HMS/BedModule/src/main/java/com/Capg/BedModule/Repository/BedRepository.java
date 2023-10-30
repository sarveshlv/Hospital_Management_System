package com.Capg.BedModule.Repository;

import com.Capg.BedModule.Model.Bed;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends MongoRepository<Bed, String> {
    List<Bed> findByHospitalId(String hospitalId);

    List<Bed> findByBedType(String bedType);
}
