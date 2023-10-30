package com.Capg.Hospital.Repository;

import com.Capg.Hospital.Constants.Address;
import com.Capg.Hospital.Constants.HospitalType;
import com.Capg.Hospital.Model.Hospital;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class HospitalRepositoryTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @Test
    public void testGetHospitalByPincode() {
        Long startPincode = 560096L;
        Long endPincode = 560098L;

        Hospital hospital = new Hospital();
        hospital.setHospitalId("abcd123");
        hospital.setHospitalName("Apollo");
        hospital.setHospitalType(HospitalType.PRIVATE);
        hospital.setHospitalAddress(createAddress());
        hospital.setPincode(560097L);
        hospital.setStatus("APPROVED");

        List<Hospital> hospitals = new ArrayList<>();
        hospitals.add(hospital);

        when(hospitalRepository.getHospitalByPincode(eq(startPincode), eq(endPincode))).thenReturn(hospitals);

        List<Hospital> resultHospitals = hospitalRepository.getHospitalByPincode(startPincode, endPincode);

        assertNotNull(resultHospitals);
        assertEquals(resultHospitals.size(), 1);

        verify(hospitalRepository, times(1)).getHospitalByPincode(eq(startPincode), eq(endPincode));
    }

    private static Address createAddress() {
        Address obj = new Address();
        obj.setStreetAddress("Basavasamithi LT");
        obj.setCityName("Bangalore");
        obj.setStateName("Karnataka");
        return obj;
    }

}
