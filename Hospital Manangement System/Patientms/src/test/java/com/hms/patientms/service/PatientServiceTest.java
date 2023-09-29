package com.hms.patientms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.hms.patientms.dto.AddPatientRequest;
import com.hms.patientms.entities.Patient;
import com.hms.patientms.exceptions.PatientNotFoundException;
import com.hms.patientms.repository.PatientRepository;

@SpringBootTest
class PatientServiceTest {

	@Mock
	private PatientRepository patientRepository;

	@Mock
	private UserApiService userApiService;

	@InjectMocks
	private PatientService patientService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddPatient() {
		// Create a sample AddPatientRequest
		AddPatientRequest request = new AddPatientRequest();
		request.setFirstName("John");
		request.setLastName("Doe");
		request.setContactNumber(1234567890L);
		request.setAadharNumber(123456789012L);
		request.setAddress(new Patient.Address("City", "Uttar Pradesh", 226022L));

		// Create a sample Patient
		Patient patient = new Patient();
		patient.setId("1");
		patient.setFirstName("John");
		patient.setLastName("Doe");
		patient.setContactNumber(1234567890L);
		patient.setAadharCard(123456789012L);

		// Mock the behavior of the repository
		when(patientRepository.save(any(Patient.class))).thenReturn(patient);
		when(userApiService.addReference(any(String.class))).thenReturn(true);
		
		// Test the addPatient method
		Patient addedPatient = patientService.addPatient(request);

		// Assertions
		assertNotNull(addedPatient);
		assertEquals(addedPatient, patient);
	}

	@Test
	void testUpdatePatient() throws PatientNotFoundException {
		// Existing patient ID
		String patientId = "1";

		// Create a sample AddPatientRequest
		AddPatientRequest request = new AddPatientRequest();
		request.setFirstName("Updated John");
		request.setLastName("Updated Doe");
		request.setContactNumber(9876543210L);
		request.setAadharNumber(987654321098L);

		// Create an existing Patient
		Patient existingPatient = new Patient();
		existingPatient.setId(patientId);
		existingPatient.setFirstName("John");
		existingPatient.setLastName("Doe");
		existingPatient.setContactNumber(1234567890L);
		existingPatient.setAadharCard(123456789012L);

		// Mock the behavior of the repository
		when(patientRepository.findById(patientId)).thenReturn(java.util.Optional.of(existingPatient));
		when(patientRepository.save(any(Patient.class))).thenReturn(existingPatient);

		// Test the updatePatient method
		Patient updatedPatient = patientService.updatePatient(patientId, request);

		// Assertions
		assertNotNull(updatedPatient);
		assertEquals("Updated John", updatedPatient.getFirstName());
		assertEquals("Updated Doe", updatedPatient.getLastName());
		assertEquals(9876543210L, updatedPatient.getContactNumber());
		assertEquals(987654321098L, updatedPatient.getAadharCard());
	}

	@Test
	void testUpdatePatientNotFoundException() {
		// Non-existing patient ID
		String patientId = "100";

		// Create a sample AddPatientRequest
		AddPatientRequest request = new AddPatientRequest();
		request.setFirstName("Updated John");
		request.setLastName("Updated Doe");
		request.setContactNumber(9876543210L);
		request.setAadharNumber(987654321098L);

		// Mock the behavior of the repository (return empty optional)
		when(patientRepository.findById(patientId)).thenReturn(java.util.Optional.empty());

		// Test the updatePatient method and expect an exception
		assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient(patientId, request));
	}

	@Test
	void testFindPatientById() throws PatientNotFoundException {
		// Existing patient ID
		String patientId = "1";

		// Create an existing Patient
		Patient existingPatient = new Patient();
		existingPatient.setId(patientId);
		existingPatient.setFirstName("John");
		existingPatient.setLastName("Doe");
		existingPatient.setContactNumber(1234567890L);
		existingPatient.setAadharCard(123456789012L);

		// Mock the behavior of the repository
		when(patientRepository.findById(patientId)).thenReturn(java.util.Optional.of(existingPatient));

		// Test the findPatientById method
		Patient foundPatient = patientService.findPatientById(patientId);

		// Assertions
		assertNotNull(foundPatient);
		assertEquals(existingPatient, foundPatient);
	}

	@Test
	void testFindPatientByIdNotFoundException() {
		// Non-existing patient ID
		String patientId = "100";

		// Mock the behavior of the repository (return empty optional)
		when(patientRepository.findById(patientId)).thenReturn(java.util.Optional.empty());

		// Test the findPatientById method and expect an exception
		assertThrows(PatientNotFoundException.class, () -> patientService.findPatientById(patientId));
	}
}
