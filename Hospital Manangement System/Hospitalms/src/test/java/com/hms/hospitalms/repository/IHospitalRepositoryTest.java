package com.hms.hospitalms.repository;

import com.hms.hospitalms.entities.Hospital;
import com.hms.hospitalms.entities.Hospital.HospitalType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
public class IHospitalRepositoryTest {

    @Autowired
    private IHospitalRepository hospitalRepository;

    // Test to find hospitals by address pincode within a specified range.
    @Test
    void testFindByAddressPincodeBetween() {
        // Create a sample private hospital.
        Hospital hospital1 = new Hospital();
        hospital1.setId("1");
        hospital1.setName("Apollo Hospital");
        hospital1.setHospitalType(HospitalType.PRIVATE);
        hospital1.setContactNo(7270043813L);
        hospital1.setAddress(new Hospital.Address("City", "Uttar Pradesh", 226022L));

        // Create a sample government hospital.
        Hospital hospital2 = new Hospital();
        hospital2.setId("2");
        hospital2.setName("Medanta Hospital");
        hospital2.setHospitalType(HospitalType.GOVT);
        hospital2.setContactNo(7270043813L);
        hospital2.setAddress(new Hospital.Address("City", "Uttar Pradesh", 226024L));

        // Save the hospitals to the database.
        hospitalRepository.saveAll(List.of(hospital1, hospital2));

        // Retrieve hospitals within the specified pincode range.
        List<Hospital> hospitalsInRange = hospitalRepository.findByAddressPincodeBetween(226021L, 226025L);

        // Assert that two hospitals are found within the range.
        assertEquals(2, hospitalsInRange.size());
    }

    // Test when there are no hospitals within the specified pincode range.
    @Test
    void testFindByAddressPincodeBetween_NoHospitalsInRange() {
        // Retrieve hospitals within a pincode range where there are no hospitals.
        List<Hospital> hospitalsInRange = hospitalRepository.findByAddressPincodeBetween(226020L, 226022L);

        // Assert that no hospitals are found in the range.
        assertEquals(0, hospitalsInRange.size());
    }
}