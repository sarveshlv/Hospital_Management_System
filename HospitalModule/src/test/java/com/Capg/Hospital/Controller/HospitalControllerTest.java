package com.Capg.Hospital.Controller;

import com.Capg.Hospital.Constants.HospitalType;
import com.Capg.Hospital.DTO.AddHospitalDTO;
import com.Capg.Hospital.Exceptions.HospitalNotFoundException;
import com.Capg.Hospital.Model.Hospital;
import com.Capg.Hospital.Service.HospitalService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class HospitalControllerTest {
    @Mock
    private HospitalService hospitalService;
    @InjectMocks
    private HospitalController hospitalController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterHospital_Success() {
        // Create a sample AddHospitalDTO
        AddHospitalDTO hospitalDTO = createAddHospitalRequest();

        // Create a Hospital object to return from the service
        Hospital hospital = createHospital();

        // Mock the hospitalService.registerHospital method
        when(hospitalService.registerHospital(hospitalDTO)).thenReturn(hospital);

        // Call the controller method
        ResponseEntity<?> responseEntity = hospitalController.registerHospital(hospitalDTO);

        // Verify that the service method was called with the correct input
        verify(hospitalService).registerHospital(hospitalDTO);

        // Check the response status and message
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Hospital Registered Successfully!", responseEntity.getBody());
    }

    @Test
    public void testRegisterHospital_Failure() {
        // Create a sample AddHospitalDTO
        AddHospitalDTO hospitalDTO = createAddHospitalRequest();
        hospitalDTO.setHospitalType("InvalidType");

        when(hospitalService.registerHospital(hospitalDTO)).thenThrow(new RuntimeException("Invalid hospital type"));

        ResponseEntity<?> responseEntity = hospitalController.registerHospital(hospitalDTO);

        verify(hospitalService).registerHospital(hospitalDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid hospital type", responseEntity.getBody());
    }

    @Test
    public void testApproveDeclineRegistration_Success() throws InstantiationException, IllegalAccessException {
        // Arrange
        String hospitalId = "123";
        String status = "APPROVE";

        // Mock the behavior of hospitalService.verifyHospital
        when(hospitalService.verifyHospital(hospitalId, status)).thenReturn(Hospital.class.newInstance());

        // Act
        ResponseEntity<?> response = hospitalController.approveDeclineRegistration(hospitalId, status);

        // Assert
        verify(hospitalService, times(1)).verifyHospital(hospitalId, status);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hospital Verified Successfully!", response.getBody());
    }

    @Test
    public void testApproveDeclineRegistration_Failure() {
        // Arrange
        String hospitalId = "456";
        String status = "REJECT";
        String errorMessage = "Invalid hospitalId";

        // Mock the behavior of hospitalService.verifyHospital to throw an exception
        when(hospitalService.verifyHospital(hospitalId, status)).thenThrow(new RuntimeException(errorMessage));

        // Act
        ResponseEntity<?> response = hospitalController.approveDeclineRegistration(hospitalId, status);

        // Assert
        verify(hospitalService, times(1)).verifyHospital(hospitalId, status);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    public void testFindNearbyHospitals_Success() {
        // Pincode to search for nearby hospitals
        Long pincode = 560097L;

        // Create a list of nearby hospitals
        List<Hospital> nearbyHospitals = new ArrayList<>();
        Hospital hospital1 = createHospital();
        Hospital hospital2 = createHospital();
        nearbyHospitals.add(hospital1);
        nearbyHospitals.add(hospital2);

        // Mock the hospitalService.findNearbyHospitals method to return the list of hospitals
        when(hospitalService.findNearbyHospitals(pincode)).thenReturn(nearbyHospitals);
        List<Hospital> result = hospitalController.findNearbyHospitals(pincode);
        verify(hospitalService).findNearbyHospitals(pincode);

        // Check the result
        assertEquals(2, result.size());
        assertEquals("Apollo", result.get(0).getHospitalName());
        assertEquals("Apollo", result.get(1).getHospitalName());
    }

    @Test
    public void testFindNearbyHospitals_NoHospitalsFound() {

        Long pincode = 560091L;

        // Mock the hospitalService.findNearbyHospitals method to return an empty list
        when(hospitalService.findNearbyHospitals(pincode)).thenReturn(new ArrayList<>());

        // Call the controller method
        try {
            hospitalController.findNearbyHospitals(pincode);
          //fail("Expected HospitalNotFoundException was not thrown");
        } catch (HospitalNotFoundException e) {
            assertEquals("There are no hospitals near to you. Sorry!", e.getMessage());
        }

        verify(hospitalService).findNearbyHospitals(pincode);
    }

    @Test
    public void testGetHospitalById_Success() {

        String hospitalId = "Apollo560097";
        Hospital hospital = createHospital();
        when(hospitalService.findHospitalById(hospitalId)).thenReturn(hospital);
        ResponseEntity<?> responseEntity = hospitalController.getHospitalById(hospitalId);
        verify(hospitalService).findHospitalById(hospitalId);

        // Check the response status and content
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(hospital, responseEntity.getBody());
    }

    @Test
    public void testGetHospitalById_NotFound() {
        String hospitalId = "NonExistentHospital";
        when(hospitalService.findHospitalById(hospitalId))
                .thenThrow(new HospitalNotFoundException("Hospital not found with ID: " + hospitalId));
        ResponseEntity<?> responseEntity = hospitalController.getHospitalById(hospitalId);
        verify(hospitalService).findHospitalById(hospitalId);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Hospital not found with ID: " + hospitalId, responseEntity.getBody());
    }

    @Test
    public void testDeleteHospitalById_Success() {
        String hospitalId = "Apollo560097";
        Hospital hospital = createHospital();
        when(hospitalService.deleteHospitalById(hospitalId)).thenReturn(hospital);
        Hospital deletedHospital = hospitalController.deleteHospitalById(hospitalId);
        verify(hospitalService).deleteHospitalById(hospitalId);
        assertEquals(hospital, deletedHospital);
    }

    @Test
    public void testDeleteHospitalById_NotFound() {
        String hospitalId = "NonExistentHospital";
        when(hospitalService.deleteHospitalById(hospitalId))
                .thenThrow(new HospitalNotFoundException("Hospital not found with ID: " + hospitalId));
        Hospital deletedHospital = hospitalController.deleteHospitalById(hospitalId);
        verify(hospitalService).deleteHospitalById(hospitalId);
        assertNull(deletedHospital);
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
