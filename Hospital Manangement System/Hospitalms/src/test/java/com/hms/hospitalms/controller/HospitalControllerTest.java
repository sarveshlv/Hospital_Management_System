package com.hms.hospitalms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.hospitalms.dto.AddHospitalRequest;
import com.hms.hospitalms.entities.Hospital;
import com.hms.hospitalms.entities.Hospital.HospitalType;
import com.hms.hospitalms.exception.HospitalNotFoundException;
import com.hms.hospitalms.service.IHospitalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This class contains unit tests for the HospitalController class.
 */
@WebMvcTest(HospitalController.class)
public class HospitalControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IHospitalService hospitalService;

    @InjectMocks
    private HospitalController hospitalController;

    /**
     * Set up the mockMvc and initialize Mockito before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(hospitalController)
                .setControllerAdvice(new CentralExceptionHandler())
                .build();
    }

    /**
     * Test the valid addition of a hospital.
     *
     * @throws Exception if there is an error during the test
     */
    @Test
    void testAddHospital_ValidRequest() throws Exception {
        // Arrange: Create a valid AddHospitalRequest and an expected Hospital object.
        AddHospitalRequest request = createAddHospitalRequest();
        Hospital expectedHospital = createHospital();

        // Mock the service method to return the expected Hospital.
        when(hospitalService.addHospital(any(AddHospitalRequest.class)))
        	.thenReturn(expectedHospital);

        // Act: Perform a POST request to add a hospital and verify the response.
        mockMvc.perform(MockMvcRequestBuilders.post("/api/hospitals/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedHospital.getId()))
                .andExpect(jsonPath("$.name").value(expectedHospital.getName()));
    }

    /**
     * Test the addition of a hospital with an invalid request.
     *
     * @throws Exception if there is an error during the test
     */
    @Test
    void testAddHospital_InvalidRequest() throws Exception {
        // Arrange: Create an invalid AddHospitalRequest with missing fields.
        AddHospitalRequest request = new AddHospitalRequest();

        // Act: Perform a POST request to add a hospital and validate the response for errors.
        mockMvc.perform(MockMvcRequestBuilders.post("/api/hospitals/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is required"))
                .andExpect(jsonPath("$.hospitalType").value("Hospital type is required"))
                .andExpect(jsonPath("$.contactNo").value("Contact number is required"))
                .andExpect(jsonPath("$.address").value("Address is required"));
    }

    /**
     * Test the update of a hospital with valid request.
     *
     * @throws Exception if there is an error during the test
     */
    @Test
    void testUpdateHospital_ValidRequest() throws Exception {
        // Arrange: Prepare the hospital ID, request, and expected result.
        String hospitalId = "1";
        AddHospitalRequest request = createAddHospitalRequest();
        Hospital expectedHospital = createHospital();

        // Mock the service method to return the expected Hospital.
        when(hospitalService.updateHospital(eq(hospitalId), any(AddHospitalRequest.class)))
                .thenReturn(expectedHospital);

        // Act: Perform a PUT request to update a hospital and verify the response.
        mockMvc.perform(MockMvcRequestBuilders.put("/api/hospitals/update/{id}", hospitalId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedHospital.getId()))
                .andExpect(jsonPath("$.name").value(expectedHospital.getName()));
    }

    /**
     * Test the update of a hospital with an invalid request.
     *
     * @throws Exception if there is an error during the test
     */
    @Test
    void testUpdateHospital_InvalidRequest() throws Exception {
        // Arrange: Prepare the hospital ID and an invalid request with missing fields.
        String hospitalId = "1";
        AddHospitalRequest request = new AddHospitalRequest(); // Empty request

        // Act: Perform a PUT request to update a hospital and validate the response for errors.
        mockMvc.perform(MockMvcRequestBuilders.put("/api/hospitals/update/{id}", hospitalId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is required"))
                .andExpect(jsonPath("$.hospitalType").value("Hospital type is required"))
                .andExpect(jsonPath("$.contactNo").value("Contact number is required"))
                .andExpect(jsonPath("$.address").value("Address is required"));
    }

    /**
     * Test retrieving a hospital by a valid ID.
     *
     * @throws Exception if there is an error during the test
     */
    @Test
    void testGetHospitalById_ValidId() throws Exception {
        // Arrange: Prepare the hospital ID and the expected hospital result.
        String hospitalId = "1";
        Hospital expectedHospital = createHospital();

        // Mock the service method to return the expected Hospital.
        when(hospitalService.findHospitalById(eq(hospitalId))).thenReturn(expectedHospital);

        // Act: Perform a GET request to retrieve a hospital by ID and verify the response.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/hospitals/find/{id}", hospitalId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedHospital.getId()))
                .andExpect(jsonPath("$.name").value(expectedHospital.getName()));
    }

    /**
     * Test retrieving a hospital by an invalid ID.
     *
     * @throws Exception if there is an error during the test
     */
    @Test
    void testGetHospitalById_InvalidId() throws Exception {
        // Arrange: Prepare an invalid hospital ID.
        String id = "2";

        // Mock the service method to throw a HospitalNotFoundException.
        when(hospitalService.findHospitalById(eq(id))).thenThrow(new HospitalNotFoundException(id));

        // Act: Perform a GET request to retrieve a hospital by an invalid ID and verify the response.
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/hospitals/find/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Hospital not found for : " + id));
    }

    
    /**
     * Test checking if a hospital is verified by a valid ID.
     *
     * @throws Exception if there is an error during the test
     */
    @Test
    void testIsHospitalVerified_ValidId() throws Exception {
        // Arrange: Prepare the hospital ID and mock the service method to return true.
        String hospitalId = "1";
        when(hospitalService.isHospitalVerified(eq(hospitalId))).thenReturn(true);

        // Act: Perform a GET request to check if a hospital is verified and verify the response.
        mockMvc.perform(MockMvcRequestBuilders.get("/api/hospitals/verified/{id}", hospitalId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));
    }

    /**
     * Test checking if a hospital is verified by an invalid ID.
     *
     * @throws Exception if there is an error during the test
     */
    @Test
    void testIsHospitalVerified_InvalidId() throws Exception {
        // Arrange: Prepare an invalid hospital ID.
        String id = "2"; // Invalid ID format

        // Mock the service method to throw a HospitalNotFoundException.
        when(hospitalService.isHospitalVerified(eq(id))).thenThrow(new HospitalNotFoundException(id));

        // Act: Perform a GET request to check if a hospital is verified with an invalid ID and verify the response.
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/hospitals/verified/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Hospital not found for : " + id));
    }

    /**
     * Test verifying a hospital by a valid ID.
     *
     * @throws Exception if there is an error during the test
     */
    @Test
    void testVerifyHospital_ValidId() throws Exception {
        // Arrange: Prepare the hospital ID, expected hospital result, and set verification to true.
        String hospitalId = "1";
        Hospital expectedHospital = createHospital();
        expectedHospital.setVerified(true);

        // Mock the service method to return the expected Hospital.
        when(hospitalService.verifyHospital(eq(hospitalId))).thenReturn(expectedHospital);

        // Act: Perform a POST request to verify a hospital by ID and verify the response.
        mockMvc.perform(MockMvcRequestBuilders.post("/api/hospitals/verify/{id}", hospitalId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expectedHospital.getId()))
                .andExpect(jsonPath("$.name").value(expectedHospital.getName()))
                .andExpect(jsonPath("$.verified").value(true));
    }

    /**
     * Test verifying a hospital by an invalid ID.
     *
     * @throws Exception if there is an error during the test
     */
    @Test
    void testVerifyHospital_InvalidId() throws Exception {
        // Arrange: Prepare an invalid hospital ID.
        String id = "2"; // Invalid ID format

        // Mock the service method to throw a HospitalNotFoundException.
        when(hospitalService.verifyHospital(eq(id))).thenThrow(new HospitalNotFoundException(id));

        // Act: Perform a POST request to verify a hospital with an invalid ID and verify the response.
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/hospitals/verify/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Hospital not found for : " + id));
    }

    /**
     * Helper method to create a sample AddHospitalRequest.
     *
     * @return a sample AddHospitalRequest
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
     * Helper method to create a sample Hospital.
     *
     * @return a sample Hospital
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
     * Helper method to convert objects to JSON string.
     *
     * @param obj the object to convert
     * @return the JSON representation of the object
     */
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}