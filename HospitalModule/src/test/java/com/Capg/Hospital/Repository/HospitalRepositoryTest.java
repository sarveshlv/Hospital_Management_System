package com.Capg.Hospital.Repository;

import com.Capg.Hospital.Constants.HospitalType;
import com.Capg.Hospital.Model.Hospital;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
public class HospitalRepositoryTest {

    @Autowired
    private HospitalRepository hospitalRepository;

    // Test to find hospitals by address pincode within a specified range.
    @Test
    void testFindByAddressPincodeBetween() {
        // Create a sample private hospital.
        Hospital hospital1 = new Hospital();
        hospital1.setHospitalId("1");
        hospital1.setHospitalName("Apollo Hospital");
        hospital1.setHospitalType(HospitalType.PRIVATE);
        hospital1.setPincode(226022L);
        hospital1.setHospitalAddress("Bangalore");

        // Create a sample government hospital.
        Hospital hospital2 = new Hospital();
        hospital2.setHospitalId("2");
        hospital2.setHospitalName("Apollo Hospital2");
        hospital2.setHospitalType(HospitalType.GOVERNMENT);
        hospital2.setPincode(226024L);
        hospital2.setHospitalAddress("Hebbal, Bangalore");

        // Save the hospitals to the database.
        hospitalRepository.saveAll(List.of(hospital1, hospital2));

        // Retrieve hospitals within the specified pincode range.
        List<Hospital> hospitalsInRange = hospitalRepository.getHospitalByPincode(226021L, 226025L);

        // Assert that two hospitals are found within the range.
        assertEquals(2, hospitalsInRange.size());
    }

    // Test when there are no hospitals within the specified pincode range.
    @Test
    void testFindByAddressPincodeBetween_NoHospitalsInRange() {
        // Retrieve hospitals within a pincode range where there are no hospitals.
        List<Hospital> hospitalsInRange = hospitalRepository.getHospitalByPincode(226020L, 226022L);

        // Assert that no hospitals are found in the range.
        assertEquals(0, hospitalsInRange.size());
    }
}
