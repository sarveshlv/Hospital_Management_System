package com.hms.bedms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.bedms.dtos.AddBedRequest;
import com.hms.bedms.entities.Bed;
import com.hms.bedms.entities.Bed.BedType;
import com.hms.bedms.dtos.UpdateBedRequest;
import com.hms.bedms.exceptions.BedNotFoundException;
import com.hms.bedms.exceptions.HospitalNotFoundException;
import com.hms.bedms.service.BedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BedController.class)
public class BedControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private BedService bedService;

	private Bed testBed;

	@BeforeEach
	void setUp() {
		// Initialize a test Bed object for use in the test cases
		testBed = new Bed();
		testBed.setId("1");
		testBed.setHospitalId("hospital123");
		testBed.setBedType(Bed.BedType.USUAL_BED);
		testBed.setCostPerDay(100.0);
	}

	@Test
	void testAdd_ValidBed() throws Exception {
		// Arrange
		AddBedRequest addBedRequest = new AddBedRequest();
		addBedRequest.setHospitalId("hospital123");
		addBedRequest.setBedType("USUAL_BED");
		addBedRequest.setCostPerDay(100.0);

		Mockito.when(bedService.addBed(any(AddBedRequest.class))).thenReturn(testBed);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/api/beds/add").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addBedRequest))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").value("1"))
				.andExpect(jsonPath("$.hospitalId").value("hospital123"))
				.andExpect(jsonPath("$.bedType").value("USUAL_BED")).andExpect(jsonPath("$.costPerDay").value(100.0));
	}

	@Test
	public void testAdd_InvalidBedWithMissingHospitalId() throws Exception {
		AddBedRequest addBedRequest = new AddBedRequest();
		addBedRequest.setBedType("USUAL_BED");
		addBedRequest.setCostPerDay(100.0);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/beds/add").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addBedRequest))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.hospitalId").value("Hospital id is required"));
	}

	@Test
	public void testAdd_InvalidBedWithInvalidBedType() throws Exception {
		AddBedRequest addBedRequest = new AddBedRequest();
		addBedRequest.setBedType("INVALID_BED");
		addBedRequest.setCostPerDay(100.0);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/beds/add").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addBedRequest))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.bedType").value("Invalid Bed type"));
	}

	@Test
	public void testAdd_InvalidBedWithNegativeCostPerDay() throws Exception {
		// Arrange
		AddBedRequest addBedRequest = new AddBedRequest();
		addBedRequest.setHospitalId("hospital123");
		addBedRequest.setBedType("INVALID_BED");
		addBedRequest.setCostPerDay(-100.0);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/beds/add").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(addBedRequest))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.costPerDay").value("Cost per day must be a positive number"));
	}

	@Test
	void testUpdateBed_ValidInput() throws Exception {
		// Arrange
		UpdateBedRequest updateBedRequest = new UpdateBedRequest();
		updateBedRequest.setBedType("ICU_BED");
		updateBedRequest.setCostPerDay(200.0);

		Bed updatedBed = testBed;
		updatedBed.setBedType(BedType.ICU_BED);
		updatedBed.setCostPerDay(200.0);

		Mockito.when(bedService.updateBed("1", updateBedRequest)).thenReturn(updatedBed);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.put("/api/beds/update/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateBedRequest))).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").value("1"))
				.andExpect(jsonPath("$.hospitalId").value("hospital123"))
				.andExpect(jsonPath("$.bedType").value("ICU_BED")).andExpect(jsonPath("$.costPerDay").value(200.0));
	}

	@Test
	void testUpdateBed_InvalidBedType() throws Exception {
		// Arrange
		UpdateBedRequest updateBedRequest = new UpdateBedRequest();
		updateBedRequest.setBedType("INVALID_BED");
		updateBedRequest.setCostPerDay(200.0);

		Mockito.when(bedService.updateBed("1", updateBedRequest)).thenReturn(null);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.put("/api/beds/update/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateBedRequest))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.bedType").value("Invalid Bed type"));
	}

	@Test
	void testUpdateBed_InvalidCostPerDay() throws Exception {
		// Arrange
		UpdateBedRequest updateBedRequest = new UpdateBedRequest();
		updateBedRequest.setBedType("ICU_BED");
		updateBedRequest.setCostPerDay(-200.0);

		Mockito.when(bedService.updateBed("1", updateBedRequest)).thenReturn(null);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.put("/api/beds/update/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateBedRequest))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.costPerDay").value("Cost per day must be a positive number"));
	}

	@Test
	void testUpdateBed_BedNotFound() throws Exception {
		// Arrange
		UpdateBedRequest updateBedRequest = new UpdateBedRequest();
		updateBedRequest.setBedType("ICU_BED");
		updateBedRequest.setCostPerDay(200.0);

		// Mock the service to throw a BedNotFoundException
		Mockito.when(bedService.updateBed("1", updateBedRequest)).thenThrow(BedNotFoundException.class);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.put("/api/beds/update/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateBedRequest))).andExpect(status().isNotFound());
	}

	@Test
	void testFindBedById_ValidBed() throws Exception {
		// Arrange
		Mockito.when(bedService.findBedById("1")).thenReturn(testBed);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/beds/findById/1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").value("1"))
				.andExpect(jsonPath("$.hospitalId").value("hospital123"))
				.andExpect(jsonPath("$.bedType").value("USUAL_BED")).andExpect(jsonPath("$.costPerDay").value(100.0));
	}

	@Test
	void testFindBedById_BedNotFound() throws Exception {
		// Arrange
		// Mock the service to throw a BedNotFoundException
		Mockito.when(bedService.findBedById("1")).thenThrow(BedNotFoundException.class);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/beds/findById/1")).andExpect(status().isNotFound());
	}

	@Test
	void testGetAllBeds() throws Exception {
		// Arrange
		List<Bed> bedList = new ArrayList<>();
		bedList.add(testBed);

		Mockito.when(bedService.getAllBeds()).thenReturn(bedList);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/beds/findAll")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$[0].id").value("1"))
				.andExpect(jsonPath("$[0].hospitalId").value("hospital123"))
				.andExpect(jsonPath("$[0].bedType").value("USUAL_BED"))
				.andExpect(jsonPath("$[0].costPerDay").value(100.0));
	}

	@Test
	void testGetNearbyBeds_ValidPincode() throws Exception {
		// Arrange
		Long validPincode = 123456L;
		List<Bed> nearbyBeds = new ArrayList<>();
		nearbyBeds.add(testBed);

		Mockito.when(bedService.getNearbyBeds(validPincode)).thenReturn(nearbyBeds);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/beds/findNearby/{pincode}", validPincode))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id").value("1")).andExpect(jsonPath("$[0].hospitalId").value("hospital123"))
				.andExpect(jsonPath("$[0].bedType").value("USUAL_BED"))
				.andExpect(jsonPath("$[0].costPerDay").value(100.0));
	}

	@Test
	void testGetNearbyBeds_InvalidPincode() throws Exception {
		// Arrange
		Long invalidPincode = 12345L; // This is not a 6-digit pincode

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/beds/findNearby/{pincode}", invalidPincode))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Pincode must be a 6-digit number"));
	}

	@Test
	void testGetBedsByType_ValidBedType() throws Exception {
		// Arrange
		String validBedType = "USUAL_BED";
		List<Bed> bedsByType = new ArrayList<>();
		bedsByType.add(testBed);

		Mockito.when(bedService.getBedsByType(validBedType)).thenReturn(bedsByType);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/beds/findByType/{bedType}", validBedType))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id").value("1")).andExpect(jsonPath("$[0].hospitalId").value("hospital123"))
				.andExpect(jsonPath("$[0].bedType").value("USUAL_BED"))
				.andExpect(jsonPath("$[0].costPerDay").value(100.0));
	}

	@Test
	void testGetBedsByType_InvalidBedType() throws Exception {
		// Arrange
		String invalidBedType = "INVALID_BED"; // This is an invalid bed type

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/beds/findByType/{bedType}", invalidBedType))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testGetBedsByHospitalId_ValidHospitalId() throws Exception {
		// Arrange
		String hospitalId = "hospital123";
		List<Bed> beds = new ArrayList<>();
		beds.add(testBed);

		when(bedService.getBedsByHospitalId(hospitalId)).thenReturn(beds);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/beds/findByHospital/{hospitalId}", hospitalId))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id").value("1")).andExpect(jsonPath("$[0].hospitalId").value("hospital123"))
				.andExpect(jsonPath("$[0].bedType").value("USUAL_BED"))
				.andExpect(jsonPath("$[0].costPerDay").value(100.0));
	}

	@Test
	public void testGetBedsByHospitalId_InvalidHospitalId() throws Exception {
		// Arrange
		String hospitalId = "invalidHospital"; // Invalid hospital ID

		when(bedService.getBedsByHospitalId(hospitalId)).thenThrow(new HospitalNotFoundException(hospitalId));

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/beds/findByHospital/{hospitalId}", hospitalId))
				.andExpect(status().isNotFound());
	}

	@Test
	void testBookBed_ValidBedId() throws Exception {
		// Arrange
		String bedId = "1";

		when(bedService.bookBed(bedId)).thenReturn(testBed);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/api/beds/bookBed/{id}", bedId)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").value("1"))
				.andExpect(jsonPath("$.hospitalId").value("hospital123"))
				.andExpect(jsonPath("$.bedType").value("USUAL_BED")).andExpect(jsonPath("$.costPerDay").value(100.0));
	}

	@Test
	void testUnbookBed_ValidBedId() throws Exception {
		// Arrange
		String bedId = "1";

		when(bedService.unbookBed(bedId)).thenReturn(testBed);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/api/beds/unbookBed/{id}", bedId)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").value("1"))
				.andExpect(jsonPath("$.hospitalId").value("hospital123"))
				.andExpect(jsonPath("$.bedType").value("USUAL_BED")).andExpect(jsonPath("$.costPerDay").value(100.0));
	}

	@Test
	void testMakeBedAvailable_ValidBedId() throws Exception {
		// Arrange
		String bedId = "1";

		when(bedService.makeBedAvailable(bedId)).thenReturn(testBed);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/api/beds/makeBedAvailable/{id}", bedId))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value("1")).andExpect(jsonPath("$.hospitalId").value("hospital123"))
				.andExpect(jsonPath("$.bedType").value("USUAL_BED")).andExpect(jsonPath("$.costPerDay").value(100.0));
	}
}