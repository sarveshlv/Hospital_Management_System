package com.hms.hospitalms.repository;

import com.hms.hospitalms.entities.Hospital;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;

@DataMongoTest
public class HospitalRepositoryTest {

    @Autowired
    private IHospitalRepository hospitalRepository;

    @MockBean
    private IHospitalRepository hospitalRepositoryMock;

    // Test case for finding hospitals by pincode range (asserts the number of hospitals)
    @Test
    public void testFindHospitalsByPincodeRange() {
        // Arrange
        Hospital hospital1 = new Hospital();
        hospital1.setId("1");
        hospital1.setName("Apollo Hospital");
        hospital1.setAddress(new Hospital.Address("Lucknow", "Uttar Pradesh", 226025L));

        Hospital hospital2 = new Hospital();
        hospital2.setId("2");
        hospital2.setName("Medanta Hospital");
        hospital2.setAddress(new Hospital.Address("Lucknow", "Uttar Pradesh", 226030L));
        
        Hospital hospital3 = new Hospital();
        hospital3.setId("3");
        hospital3.setName("Lohiya Hospital");
        hospital3.setAddress(new Hospital.Address("Lucknow", "Uttar Pradesh", 226035L));

        Hospital hospital4 = new Hospital();
        hospital4.setId("4");
        hospital4.setName("SGPGI Hospital");
        hospital4.setAddress(new Hospital.Address("Lucknow", "Uttar Pradesh", 226040L));


        List<Hospital> hospitalsInRange = List.of(hospital1, hospital2, hospital3, hospital4);

        // Mock
        Mockito.when(hospitalRepositoryMock.findByAddressPincodeBetween(anyLong(), anyLong())).thenReturn(hospitalsInRange);

        // Act
        List<Hospital> actualHospitals = hospitalRepository.findByAddressPincodeBetween(226030L, 226040L);

        // Assert
        assertEquals(4, actualHospitals.size());
    }

    // Test case for finding hospitals by pincode range when no hospitals are found (asserts an empty list)
    @Test
    public void testFindHospitalsByPincodeRangeNoResults() {
        // Mock
        Mockito.when(hospitalRepositoryMock.findByAddressPincodeBetween(anyLong(), anyLong())).thenReturn(List.of());

        // Act
        List<Hospital> hospitals = hospitalRepository.findByAddressPincodeBetween(100000L, 800000L);

        // Assert
        assertEquals(0, hospitals.size());
    }
}