package com.hms.patientms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

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

	@InjectMocks
	private PatientService patientService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

    // Test case for adding a new patient successfully
	@Test
	void testAddPatient() {
		//Arrange
		AddPatientRequest request = new AddPatientRequest();
		request.setFirstName("Prateek");
		request.setLastName("Singh");
		request.setContactNumber(7270043813L);
		request.setAadharNumber(867343001999L);
		request.setAddress(new Patient.Address("Lucknow", "Uttar Pradesh", 226022L));

		// Create a sample Patient
		Patient patient = new Patient();
		patient.setId("1");
		patient.setFirstName("Prateek");
		patient.setLastName("Singh");
		patient.setContactNumber(7270043813L);
		patient.setAadharCard(867343001999L);

		// Mock
		when(patientRepository.save(any(Patient.class))).thenReturn(patient);
		
		//Act
		Patient addedPatient = patientService.addPatient(request);

		// Assert
		assertNotNull(addedPatient);
		assertEquals(addedPatient, patient);
	}

    // Test case for updating an existing patient successfully
	@Test
	void testUpdatePatient() throws PatientNotFoundException {
		//Arrange
		String patientId = "1";

		//Mock
		AddPatientRequest request = new AddPatientRequest();
		request.setFirstName("Updated Prateek");
		request.setLastName("Updated Singh");
		request.setContactNumber(7270043813L);
		request.setAadharNumber(867343001999L);
		Patient existingPatient = new Patient();
		existingPatient.setId(patientId);
		existingPatient.setFirstName("Prateek");
		existingPatient.setLastName("Singh");
		existingPatient.setContactNumber(7270043813L);
		existingPatient.setAadharCard(867343001999L);

		// Mock
		when(patientRepository.findById(patientId)).thenReturn(java.util.Optional.of(existingPatient));
		when(patientRepository.save(any(Patient.class))).thenReturn(existingPatient);

		//Act
		Patient updatedPatient = patientService.updatePatient(patientId, request);

		// Assert
		assertNotNull(updatedPatient);
		assertEquals("Updated Prateek", updatedPatient.getFirstName());
		assertEquals("Updated Singh", updatedPatient.getLastName());
		assertEquals(7270043813L, updatedPatient.getContactNumber());
		assertEquals(867343001999L, updatedPatient.getAadharCard());
	}

    // Test case for attempting to update a patient that doesn't exist
	@Test
	void testUpdatePatientNotFoundException() {
		//Arrange
		String patientId = "2";

		//Mock
		AddPatientRequest request = new AddPatientRequest();
		request.setFirstName("Prateek");
		request.setLastName("Singh");
		request.setContactNumber(7270043813L);
		request.setAadharNumber(86743001999L);
		when(patientRepository.findById(patientId)).thenReturn(java.util.Optional.empty());

		//Act and Assert
		assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient(patientId, request));
	}

    // Test case for finding a patient by ID successfully
	@Test
	void testFindPatientById() throws PatientNotFoundException {
		//Arrange
		String patientId = "1";

		//Mock
		Patient existingPatient = new Patient();
		existingPatient.setId(patientId);
		existingPatient.setFirstName("Prateek");
		existingPatient.setLastName("Singh");
		existingPatient.setContactNumber(7270043813L);
		existingPatient.setAadharCard(867343001999L);
		when(patientRepository.findById(patientId)).thenReturn(java.util.Optional.of(existingPatient));

		//Act
		Patient foundPatient = patientService.findPatientById(patientId);

		//Assert
		assertNotNull(foundPatient);
		assertEquals(existingPatient, foundPatient);
	}

    // Test case for attempting to find a patient by ID that doesn't exist
	@Test
	void testFindPatientByIdNotFoundException() {
		//Arrange
		String patientId = "2";

		//Act and Mock
		when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

		//Asset
		assertThrows(PatientNotFoundException.class, () -> patientService.findPatientById(patientId));
	}
}
