package com.hms.patientms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.patientms.dto.AddPatientRequest;
import com.hms.patientms.entities.Patient;
import com.hms.patientms.exceptions.PatientNotFoundException;
import com.hms.patientms.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PatientControllerTest {

	private MockMvc mockMvc;

	@Mock
	private PatientService patientService;

	@InjectMocks
	private PatientController patientController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(patientController).setControllerAdvice(new CentralExceptionHandler())
				.build();
	}

    // Test case to validate adding a patient with a valid request
	@Test
	void testAddPatient_ValidRequest() throws Exception {
		// Arrange
		AddPatientRequest request = createAddPatientRequest();
		Patient expectedPatient = createPatient();

		// Mock
		when(patientService.addPatient(any(AddPatientRequest.class))).thenReturn(expectedPatient);

		// Act + Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/api/patients/add").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(request))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(expectedPatient.getId()))
				.andExpect(jsonPath("$.firstName").value(expectedPatient.getFirstName()));
		
	}

    // Test case to validate adding a patient with an invalid request
	@Test
	void testAddPatient_InvalidRequest() throws Exception {
		// Arrange
		AddPatientRequest request = new AddPatientRequest();

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/api/patients/add").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(request))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.firstName").value("First Name is required"))
				.andExpect(jsonPath("$.lastName").value("First Name is required"))
				.andExpect(jsonPath("$.contactNumber").value("Contact number is required"))
				.andExpect(jsonPath("$.aadharNumber").value("Aadhar number is required"))
				.andExpect(jsonPath("$.address").value("Address is required"));

	}

    // Test case to validate updating a patient with a valid request
	@Test
	void testUpdatePatient_ValidRequest() throws Exception {
		// Arrange
		String patientId = "1";
		AddPatientRequest request = createAddPatientRequest();
		Patient expectedPatient = createPatient();

		// Mock
		when(patientService.updatePatient(eq(patientId), any(AddPatientRequest.class))).thenReturn(expectedPatient);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.put("/api/patients/update/{id}", patientId)
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(request))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(expectedPatient.getId()))
				.andExpect(jsonPath("$.firstName").value(expectedPatient.getFirstName()));
	}

    // Test case to validate updating a patient with an invalid request
	@Test
	void testUpdatePatient_InvalidRequest() throws Exception {
		// Arrange
		String patientId = "1";
		AddPatientRequest request = new AddPatientRequest();

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.put("/api/patients/update/{id}", patientId)
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.firstName").value("First Name is required"))
				.andExpect(jsonPath("$.lastName").value("First Name is required"))
				.andExpect(jsonPath("$.contactNumber").value("Contact number is required"))
				.andExpect(jsonPath("$.aadharNumber").value("Aadhar number is required"))
				.andExpect(jsonPath("$.address").value("Address is required"));
	}

    // Test case to validate finding a patient by a valid ID
	@Test
	void testFindPatientById_ValidId() throws Exception {
		// Arrange
		String patientId = "1";
		Patient expectedPatient = createPatient();

		// Mock
		when(patientService.findPatientById(eq(patientId))).thenReturn(expectedPatient);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/patients/findById/{id}", patientId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(expectedPatient.getId()))
				.andExpect(jsonPath("$.firstName").value(expectedPatient.getFirstName()));
	}

    // Test case to validate finding a patient by an invalid ID
	@Test
	void testFindPatientById_InvalidId() throws Exception {
		// Arrange
		String id = "2";

		// Mock
		when(patientService.findPatientById(eq(id))).thenThrow(new PatientNotFoundException(id));

		// Act + Assert
		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/patients/findById/{id}", id).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.error").value("Patient not found for : " + id));
	}

    // Test case to validate finding a patient by an invalid ID
	private static AddPatientRequest createAddPatientRequest() {
		AddPatientRequest request = new AddPatientRequest();
		request.setFirstName("Prateek");
		request.setLastName("Singh");
		request.setContactNumber(7270043813L);
		request.setAadharNumber(867343001999L);
		request.setAddress(new Patient.Address("Lucknow", "Uttar Pradesh", 226022L));
		return request;
	}

	private static Patient createPatient() {
		Patient patient = new Patient();
		patient.setId("1");
		patient.setFirstName("Prateek");
		patient.setLastName("Singh");
		patient.setContactNumber(7270043813L);
		patient.setAadharCard(867343001999L);
		patient.setAddress(new Patient.Address("Lucknow", "Uttar Pradesh", 226022L));
		return patient;
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
