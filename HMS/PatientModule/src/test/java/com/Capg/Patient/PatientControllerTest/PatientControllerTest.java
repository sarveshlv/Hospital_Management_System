package com.Capg.Patient.PatientControllerTest;


import com.Capg.Patient.Constants.Address;
import com.Capg.Patient.Controller.PatientController;
import com.Capg.Patient.DTO.AddPatientDTO;
import com.Capg.Patient.Exception.CentralExceptionHandler;
import com.Capg.Patient.Model.Patient;
import com.Capg.Patient.Service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).setControllerAdvice(new CentralExceptionHandler())
                .build();
    }

    @Test
    public void testAddPatient() throws Exception {
        AddPatientDTO addPatientDTO = createAddPatientDTO();
        Patient patient = createPatient();

        when(patientService.addPatient(any(AddPatientDTO.class))).thenReturn(patient);

        mockMvc.perform(post("/Patient/addPatient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addPatientDTO)))
                .andExpect(status().isOk()) // You can change the expected status code as needed
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(patient.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(patient.getLastName()));// Replace with actual expected values

        verify(patientService, times(1)).addPatient(any(AddPatientDTO.class));
    }

    @Test
    public void testUpdatePatient() throws Exception {
        String patientId = "abcd1234"; // Replace with an actual patient ID
        AddPatientDTO addPatientDTO = createAddPatientDTO();

        Patient patient = createPatient();

        when(patientService.updatePatient(eq(patientId), any(AddPatientDTO.class))).thenReturn(patient);

        mockMvc.perform(put("/Patient/updatePatient/{id}", patientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addPatientDTO)))
                .andExpect(status().isOk()) // You can change the expected status code as needed
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(patient.getFirstName()));

        verify(patientService, times(1)).updatePatient(eq(patientId), any(AddPatientDTO.class));
    }
//
    @Test
    public void testFindPatient() throws Exception {
        String patientId = "abcd1234"; // Replace with an actual patient ID
        Patient patient = createPatient();

        when(patientService.findPatientById(eq(patientId))).thenReturn(patient);

        mockMvc.perform(get("/Patient/findPatient/{patientId}", patientId))
                .andExpect(status().isOk()) // You can change the expected status code as needed
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(patient.getFirstName()));

        verify(patientService, times(1)).findPatientById(eq(patientId));
    }

    // Utility method to convert object to JSON string
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private static Patient createPatient() {
        Patient obj = new Patient();
        obj.setPatientId("abcd1234");
        obj.setFirstName("Sarvesh");
        obj.setLastName("LV");
        obj.setContactNumber(9482729175L);
        obj.setAadharCard(928243121710L);
        obj.setAddress(createAddress());
        obj.setPincode(560097L);
        return obj;
    }

    private static Address createAddress() {
        Address obj = new Address();
        obj.setStreetAddress("Basavasamithi LT");
        obj.setCityName("Bangalore");
        obj.setStateName("Karnataka");
        return obj;
    }

    private static AddPatientDTO createAddPatientDTO() {
        AddPatientDTO obj = new AddPatientDTO();
        obj.setFirstName("Sarvesh");
        obj.setLastName("LV");
        obj.setContactNumber(9482729175L);
        obj.setAadharNumber(928243121710L);
        obj.setAddress(createAddress());
        obj.setPincode(560097L);
        return obj;
    }
}
