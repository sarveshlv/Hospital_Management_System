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

	@Test
	void testAddPatient_ValidRequest() throws Exception {
		// Arrange: Create a valid AddPatientRequest and an expected Patient object.
		AddPatientRequest request = createAddPatientRequest();
		Patient expectedPatient = createPatient();

		// Mock the service method to return the expected Patient.
		when(patientService.addPatient(any(AddPatientRequest.class))).thenReturn(expectedPatient);

		// Act: Perform a POST request to add a patient and verify the response.
		mockMvc.perform(MockMvcRequestBuilders.post("/api/patients/add").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(request))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(expectedPatient.getId()))
				.andExpect(jsonPath("$.firstName").value(expectedPatient.getFirstName()));
	}

	@Test
	void testAddPatient_InvalidRequest() throws Exception {
		// Arrange: Create an invalid AddPatientRequest with missing fields.
		AddPatientRequest request = new AddPatientRequest();

		// Act: Perform a POST request to add a patient and validate the response for
		// errors.
		mockMvc.perform(MockMvcRequestBuilders.post("/api/patients/add").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(request))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.firstName").value("First Name is required"))
				.andExpect(jsonPath("$.lastName").value("First Name is required"))
				.andExpect(jsonPath("$.contactNumber").value("Contact number is required"))
				.andExpect(jsonPath("$.aadharNumber").value("Aadhar number is required"))
				.andExpect(jsonPath("$.address").value("Address is required"));

	}

	@Test
	void testUpdatePatient_ValidRequest() throws Exception {
		// Arrange: Prepare the patient ID, request, and expected result.
		String patientId = "1";
		AddPatientRequest request = createAddPatientRequest();
		Patient expectedPatient = createPatient();

		// Mock the service method to return the expected Patient.
		when(patientService.updatePatient(eq(patientId), any(AddPatientRequest.class))).thenReturn(expectedPatient);

		// Act: Perform a PUT request to update a patient and verify the response.
		mockMvc.perform(MockMvcRequestBuilders.put("/api/patients/update/{id}", patientId)
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(request))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(expectedPatient.getId()))
				.andExpect(jsonPath("$.firstName").value(expectedPatient.getFirstName()));
	}

	@Test
	void testUpdatePatient_InvalidRequest() throws Exception {
		// Arrange: Prepare the patient ID and an invalid request with missing fields.
		String patientId = "1";
		AddPatientRequest request = new AddPatientRequest(); // Empty request

		// Act: Perform a PUT request to update a patient and validate the response for
		// errors.
		mockMvc.perform(MockMvcRequestBuilders.put("/api/patients/update/{id}", patientId)
				.contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.firstName").value("First Name is required"))
				.andExpect(jsonPath("$.lastName").value("First Name is required"))
				.andExpect(jsonPath("$.contactNumber").value("Contact number is required"))
				.andExpect(jsonPath("$.aadharNumber").value("Aadhar number is required"))
				.andExpect(jsonPath("$.address").value("Address is required"));
	}

	@Test
	void testFindPatientById_ValidId() throws Exception {
		// Arrange: Prepare the patient ID and the expected patient result.
		String patientId = "1";
		Patient expectedPatient = createPatient();

		// Mock the service method to return the expected Patient.
		when(patientService.findPatientById(eq(patientId))).thenReturn(expectedPatient);

		// Act: Perform a GET request to retrieve a patient by ID and verify the
		// response.
		mockMvc.perform(MockMvcRequestBuilders.get("/api/patients/find/{id}", patientId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(expectedPatient.getId()))
				.andExpect(jsonPath("$.firstName").value(expectedPatient.getFirstName()));
	}

	@Test
	void testFindPatientById_InvalidId() throws Exception {
		// Arrange: Prepare an invalid patient ID.
		String id = "2"; // Invalid ID format

		// Mock the service method to throw a PatientNotFoundException.
		when(patientService.findPatientById(eq(id))).thenThrow(new PatientNotFoundException(id));

		// Act: Perform a GET request to retrieve a patient by an invalid ID and verify
		// the response.
		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/patients/find/{id}", id).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.error").value("Patient not found for : " + id));
	}

	private static AddPatientRequest createAddPatientRequest() {
		AddPatientRequest request = new AddPatientRequest();
		request.setFirstName("John");
		request.setLastName("Doe");
		request.setContactNumber(7270043813L);
		request.setAadharNumber(123456789012L);
		request.setAddress(new Patient.Address("City", "State", 123456L));
		return request;
	}

	private static Patient createPatient() {
		Patient patient = new Patient();
		patient.setId("1");
		patient.setFirstName("John");
		patient.setLastName("Doe");
		patient.setContactNumber(7270043813L);
		patient.setAadharCard(123456789012L);
		patient.setAddress(new Patient.Address("City", "State", 123456L));
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