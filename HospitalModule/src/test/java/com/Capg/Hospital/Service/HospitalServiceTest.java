package com.Capg.Hospital.Service;

import com.Capg.Hospital.Constants.HospitalType;
import com.Capg.Hospital.DTO.AddHospitalDTO;
import com.Capg.Hospital.Exceptions.HospitalNotFoundException;
import com.Capg.Hospital.Model.Hospital;
import com.Capg.Hospital.Repository.HospitalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class HospitalServiceTest {
    @Mock
    private HospitalRepository hospitalRepository;
    @InjectMocks
    private HospitalServiceImpl hospitalService;

    @BeforeEach
    void setUp() {
        // Initialize Mockito mocks before each test method.
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterHospital() {
        AddHospitalDTO hospitalDTO = createAddHospitalRequest();
        Hospital expectedHospital = createHospital();
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(expectedHospital);
        Hospital savedHospital = hospitalService.registerHospital(hospitalDTO);
        assertEquals(expectedHospital, savedHospital);
    }

    @Test
    public void testVerifyHospitalApprove() {
        // Create a sample hospitalId and status
        String hospitalId = "Apollo560097";
        String status = "APPROVE";

        // Create a sample Hospital object that you expect to be saved after approval
        Hospital existingHospital = createHospital();

        // Mock the behavior of the hospitalRepository.findById() method
        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(existingHospital));

        // Call the verifyHospital method
        Hospital approvedHospital = hospitalService.verifyHospital(hospitalId, status);

        // Assert that the status has been updated to "APPROVED"
        assertEquals("APPROVED", approvedHospital.getStatus());

        // Verify that hospitalRepository.save() was called
        verify(hospitalRepository, times(1)).save(existingHospital);
    }

    @Test
    public void testVerifyHospitalReject() {
        // Create a sample hospitalId and status
        String hospitalId = "TestHospital12345";
        String status = "REJECT";

        // Create a sample Hospital object that you expect to be unchanged after rejection
        Hospital existingHospital = new Hospital();
        existingHospital.setHospitalId(hospitalId);
        existingHospital.setStatus("Requested");

        // Mock the behavior of the hospitalRepository.findById() method
        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(existingHospital));

        // Call the verifyHospital method
        Hospital rejectedHospital = hospitalService.verifyHospital(hospitalId, status);

        // Assert that the status remains "Requested" and that the returned hospital is null
        assertEquals("Requested", rejectedHospital.getStatus());
        assertNull(rejectedHospital);

        // Verify that hospitalRepository.save() was not called
        verify(hospitalRepository, never()).save(any(Hospital.class));
    }

    @Test
    public void testVerifyHospitalHospitalNotFound() {
        // Create a sample hospitalId and status
        String hospitalId = "NonExistentHospital";
        String status = "APPROVE";

        // Mock the behavior of the hospitalRepository.findById() method to return an empty Optional
        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.empty());

        // Call the verifyHospital method, which should throw HospitalNotFoundException
        hospitalService.verifyHospital(hospitalId, status);
    }

    private static AddHospitalDTO createAddHospitalRequest() {
        AddHospitalDTO request = new AddHospitalDTO();
        request.setHospitalName("Apollo");
        request.setHospitalAddress("Yeshtwantpur, Bangalore");
        request.setPincode(560097L);
        request.setHospitalType("PRIVATE");

        return request;
    }

    private static Hospital createHospital() {
        Hospital hospital = new Hospital();
        hospital.setHospitalId("Apollo560097");
        hospital.setHospitalName("Apollo");
        hospital.setHospitalType(HospitalType.PRIVATE);
        hospital.setHospitalAddress("Yeshtwantpur, Bangalore");
        hospital.setPincode(560097L);
        hospital.setStatus("Requested");
        return hospital;
    }
}
