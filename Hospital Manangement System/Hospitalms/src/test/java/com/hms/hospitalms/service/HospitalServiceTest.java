/**
 * This package contains test cases for the HospitalService class.
 */
package com.hms.hospitalms.service;

import com.hms.hospitalms.dto.AddHospitalRequest;
import com.hms.hospitalms.entities.Hospital;
import com.hms.hospitalms.entities.Hospital.HospitalType;
import com.hms.hospitalms.exception.HospitalNotFoundException;
import com.hms.hospitalms.repository.IHospitalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the HospitalService class. Author: @prateek
 */
@SpringBootTest
public class HospitalServiceTest {

	@Mock
	private IHospitalRepository hospitalRepository;


	@InjectMocks
	private HospitalService hospitalService;

	@BeforeEach
	void setUp() {
		// Initialize Mockito mocks before each test method.
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Test case for adding a new hospital.
	 */
	@Test
	void testAddHospital() {
		// Prepare test data
		AddHospitalRequest request = createAddHospitalRequest();
		Hospital expectedHospital = createHospital();

		// Mock the behavior of the hospitalRepository
		when(hospitalRepository.save(any(Hospital.class)))
			.thenReturn(expectedHospital);

		// Invoke the service method and assert the result
		assertEquals(expectedHospital, hospitalService.addHospital(request));

		// Verify that the hospitalRepository.save method was called once
		verify(hospitalRepository, times(1)).save(any(Hospital.class));
	}

	/**
	 * Test case for updating an existing hospital.
	 */
	@Test
	void testUpdateHospital() {
		// Prepare test data
		String hospitalId = "1";
		AddHospitalRequest request = createAddHospitalRequest();
		Hospital existingHospital = createHospital();

		// Mock the behavior of the hospitalRepository
		when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(existingHospital));
		when(hospitalRepository.save(any(Hospital.class))).thenReturn(existingHospital);

		// Invoke the service method
		Hospital updatedHospital = hospitalService.updateHospital(hospitalId, request);

		// Assert the updated fields
		assertEquals(request.getName(), updatedHospital.getName());
		assertEquals(request.getHospitalType(), updatedHospital.getHospitalType().toString());

		// Verify that findById and save methods were called
		verify(hospitalRepository, times(1)).findById(hospitalId);
		verify(hospitalRepository, times(1)).save(any(Hospital.class));
	}

	/**
	 * Test case for updating a hospital when it's not found.
	 */
	@Test
	void testUpdateHospitalHospitalNotFound() {
		// Prepare test data
		String hospitalId = "1";
		AddHospitalRequest request = createAddHospitalRequest();

		// Mock the behavior of the hospitalRepository to return empty result
		when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.empty());

		// Verify that HospitalNotFoundException is thrown
		assertThrows(HospitalNotFoundException.class, () -> hospitalService.updateHospital(hospitalId, request));

		// Verify that findById was called once and save was never called
		verify(hospitalRepository, times(1)).findById(hospitalId);
		verify(hospitalRepository, never()).save(any(Hospital.class));
	}

	/**
	 * Test case for finding a hospital by ID.
	 */
	@Test
	void testFindHospitalById() {
		// Prepare test data
		String hospitalId = "1";
		Hospital expectedHospital = createHospital();

		// Mock the behavior of the hospitalRepository
		when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(expectedHospital));

		// Invoke the service method and assert the result
		Hospital result = hospitalService.findHospitalById(hospitalId);

		assertEquals(expectedHospital, result);

		// Verify that findById was called once
		verify(hospitalRepository, times(1)).findById(hospitalId);
	}

	/**
	 * Test case for finding a hospital by ID when it's not found.
	 */
	@Test
	void testFindHospitalByIdHospitalNotFound() {
		// Prepare test data
		String hospitalId = "1";

		// Mock the behavior of the hospitalRepository to return empty result
		when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.empty());

		// Verify that HospitalNotFoundException is thrown
		assertThrows(HospitalNotFoundException.class, () -> hospitalService.findHospitalById(hospitalId));

		// Verify that findById was called once
		verify(hospitalRepository, times(1)).findById(hospitalId);
	}

	
	
	/**
	 * Test case for checking if a hospital is verified.
	 */
	@Test
	void testIsHospitalVerified() {
		// Prepare test data
		String hospitalId = "1";
		Hospital hospital = createHospital();
		hospital.setVerified(true);

		// Mock the behavior of the hospitalRepository
		when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(hospital));

		// Invoke the service method and assert the result
		Boolean result = hospitalService.isHospitalVerified(hospitalId);
		assertTrue(result);

		// Verify that findById was called once
		verify(hospitalRepository, times(1)).findById(hospitalId);
	}

	/**
	 * Test case for checking if a hospital is not verified.
	 */
	@Test
	void testIsHospitalNotVerified() {
		// Prepare test data
		String hospitalId = "1";
		Hospital hospital = createHospital();
		hospital.setVerified(false);

		// Mock the behavior of the hospitalRepository
		when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(hospital));

		// Invoke the service method and assert the result
		Boolean result = hospitalService.isHospitalVerified(hospitalId);
		assertFalse(result);

		// Verify that findById was called once
		verify(hospitalRepository, times(1)).findById(hospitalId);
	}

	/**
	 * Test case for checking if a hospital is verified when it's not found.
	 */
	@Test
	void testIsHospitalVerifiedHospitalNotFound() {
		// Prepare test data
		String hospitalId = "1";

		// Mock the behavior of the hospitalRepository to return empty result
		when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.empty());

		// Verify that HospitalNotFoundException is thrown
		assertThrows(HospitalNotFoundException.class, () -> hospitalService.isHospitalVerified(hospitalId));

		// Verify that findById was called once
		verify(hospitalRepository, times(1)).findById(hospitalId);
	}

	/**
	 * Test case for verifying a hospital.
	 */
	@Test
	void testVerifyHospital() {
		// Prepare test data
		String hospitalId = "1";
		Hospital hospital = createHospital();

		// Mock the behavior of the hospitalRepository
		when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(hospital));
		when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospital);

		// Invoke the service method and assert the result
		Hospital verifiedHospital = hospitalService.verifyHospital(hospitalId);

		assertTrue(verifiedHospital.getVerified());

		// Verify that findById and save methods were called
		verify(hospitalRepository, times(1)).findById(hospitalId);
		verify(hospitalRepository, times(1)).save(any(Hospital.class));
	}

	/**
	 * Test case for verifying a hospital when it's not found.
	 */
	@Test
	void testVerifyHospitalHospitalNotFound() {
		// Prepare test data
		String hospitalId = "1";

		// Mock the behavior of the hospitalRepository to return empty result
		when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.empty());

		// Verify that HospitalNotFoundException is thrown
		assertThrows(HospitalNotFoundException.class, () -> hospitalService.verifyHospital(hospitalId));

		// Verify that save was never called
		verify(hospitalRepository, never()).save(any(Hospital.class));
	}

	/**
	 * Test case for getting nearby hospitals.
	 */
	@Test
	void testGetNearbyHospitals() {
		// Prepare test data
		Long pincode = 226022L;
		List<Hospital> nearbyHospitals = createAndSaveHospitalList();

		// Mock the behavior of the hospitalRepository
		when(hospitalRepository.findByAddressPincodeBetween(pincode - 2, pincode + 2)).thenReturn(nearbyHospitals);

		// Invoke the service method and assert the result
		assertEquals(nearbyHospitals, hospitalService.getNearbyHospitals(pincode));

		// Verify that findByAddressPincodeBetween was called once
		verify(hospitalRepository, times(1)).findByAddressPincodeBetween(pincode - 2, pincode + 2);
	}

	/**
	 * Helper method for creating an AddHospitalRequest object.
	 * 
	 * This method creates and returns an AddHospitalRequest object with predefined
	 * attributes for testing purposes. It sets the name, hospital type, contact
	 * number, and address for the request.
	 *
	 * @return An AddHospitalRequest object with predefined attributes.
	 */
	private static AddHospitalRequest createAddHospitalRequest() {
		AddHospitalRequest request = new AddHospitalRequest();
		request.setName("Apollo Hospital");
		request.setHospitalType("PRIVATE");
		request.setContactNo(7270043813L);
		request.setAddress(new Hospital.Address("Lucknow", "Uttar Pradesh", 226022L));
		return request;
	}

	/**
	 * Helper method for creating a Hospital object.
	 * 
	 * This method creates and returns a Hospital object with predefined attributes
	 * for testing purposes. It sets the hospital ID, name, type, contact number,
	 * and address.
	 *
	 * @return A Hospital object with predefined attributes.
	 */
	private static Hospital createHospital() {
		Hospital hospital = new Hospital();
		hospital.setId("1");
		hospital.setName("Apollo Hospital");
		hospital.setHospitalType(HospitalType.PRIVATE);
		hospital.setContactNo(7270043813L);
		hospital.setAddress(new Hospital.Address("City", "Uttar Pradesh", 226022L));
		return hospital;
	}

	/**
	 * Helper method for creating and saving a list of hospitals.
	 * 
	 * This method creates a list of Hospital objects, saves them to the hospital
	 * repository, and returns the list of hospitals. It is used to simulate the
	 * presence of multiple hospitals in the repository for testing.
	 *
	 * @return A list of Hospital objects that have been created and saved.
	 */
	private List<Hospital> createAndSaveHospitalList() {
		List<Hospital> hospitals = new ArrayList<>();
		Hospital hospital1 = new Hospital();
		hospital1.setId("1");
		hospital1.setName("Apollo Hospital");
		hospital1.setHospitalType(HospitalType.PRIVATE);
		hospital1.setContactNo(7270043813L);
		hospital1.setAddress(new Hospital.Address("City", "Uttar Pradesh", 226022L));

		Hospital hospital2 = new Hospital();
		hospital2.setId("2");
		hospital2.setName("Medanta Hospital");
		hospital2.setHospitalType(HospitalType.GOVT);
		hospital2.setContactNo(7270043813L);
		hospital2.setAddress(new Hospital.Address("City", "Uttar Pradesh", 226024L));

		hospitalRepository.saveAll(List.of(hospital1, hospital2));

		hospitals.add(hospital1);
		hospitals.add(hospital2);
		return hospitals;
	}
}