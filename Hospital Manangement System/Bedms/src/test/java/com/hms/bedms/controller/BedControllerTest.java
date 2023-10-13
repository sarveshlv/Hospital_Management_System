package com.hms.bedms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hms.bedms.dtos.AddBedRequest;
import com.hms.bedms.entities.Bed;
import com.hms.bedms.dtos.UpdateBedRequest;
import com.hms.bedms.exceptions.BedNotFoundException;
import com.hms.bedms.exceptions.HospitalNotFoundException;
import com.hms.bedms.service.BedService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BedControllerTest {

	private MockMvc mockMvc;

	@Mock
	private BedService bedService;

	@InjectMocks
	private BedController bedController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(bedController).setControllerAdvice(new CentralExceptionHandler())
				.build();
	}

	@Test
	public void testAdd_InvalidBedWithMissingHospitalId() throws Exception {
		AddBedRequest addBedRequest = new AddBedRequest();
		addBedRequest.setBedType("USUAL_BED");
		addBedRequest.setCostPerDay(100.0);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/beds/add").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(addBedRequest))).andExpect(status().isBadRequest());
	}

	@Test
	public void testAdd_InvalidBedWithInvalidBedType() throws Exception {
		AddBedRequest addBedRequest = new AddBedRequest();
		addBedRequest.setBedType("INVALID_BED");
		addBedRequest.setCostPerDay(100.0);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/beds/add").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(addBedRequest))).andExpect(status().isBadRequest());
//				.andExpect(jsonPath("$.bedType").value("Invalid Bed type"));
	}

	@Test
	public void testAdd_InvalidBedWithNegativeCostPerDay() throws Exception {
		// Arrange
		AddBedRequest addBedRequest = new AddBedRequest();
		addBedRequest.setHospitalId("hospital123");
		addBedRequest.setBedType("INVALID_BED");
		addBedRequest.setCostPerDay(-100.0);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/beds/add").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(addBedRequest))).andExpect(status().isBadRequest());
//				.andExpect(jsonPath("$.costPerDay").value("Cost per day must be a positive number"));
	}

	@Test
	void testUpdateBed_ValidInput() throws Exception {
		// Arrange
		UpdateBedRequest updateBedRequest = new UpdateBedRequest();
		updateBedRequest.setBedType("ICU_BED");
		updateBedRequest.setCostPerDay(200.0);

		// Mock
		Bed updatedBed = new Bed();
		updatedBed.setId("1");
		updatedBed.setHospitalId("hospital123");
		updatedBed.setBedType(Bed.BedType.ICU_BED);
		updatedBed.setCostPerDay(200.0);
		Mockito.when(bedService.updateBed("1", updateBedRequest)).thenReturn(updatedBed);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.put("/api/beds/update/1").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(updateBedRequest))).andExpect(status().isOk())
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
				.content(asJsonString(updateBedRequest))).andExpect(status().isBadRequest())
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
				.content(asJsonString(updateBedRequest))).andExpect(status().isBadRequest())
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
				.content(asJsonString(updateBedRequest))).andExpect(status().isNotFound());
	}

	@Test
	void testFindBedById_ValidBed() throws Exception {
		// Arrange
		Bed testBed = new Bed();
		testBed.setId("1");
		testBed.setHospitalId("hospital123");
		testBed.setBedType(Bed.BedType.ICU_BED);
		testBed.setCostPerDay(200.0);

		// Mock
		Mockito.when(bedService.findBedById("1")).thenReturn(testBed);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/beds/findById/1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").value("1"))
				.andExpect(jsonPath("$.hospitalId").value("hospital123"))
				.andExpect(jsonPath("$.bedType").value("ICU_BED")).andExpect(jsonPath("$.costPerDay").value(200.0));
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
		Bed testBed = new Bed();
		testBed.setId("1");
		testBed.setHospitalId("hospital123");
		testBed.setBedType(Bed.BedType.ICU_BED);
		testBed.setCostPerDay(200.0);

		// Mock
		List<Bed> bedList = new ArrayList<>();
		bedList.add(testBed);
		Mockito.when(bedService.getAllBeds()).thenReturn(bedList);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/beds/findAll")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$[0].id").value("1"))
				.andExpect(jsonPath("$[0].hospitalId").value("hospital123"))
				.andExpect(jsonPath("$[0].bedType").value("ICU_BED"))
				.andExpect(jsonPath("$[0].costPerDay").value(200.0));
	}

	@Test
	void testGetBedsByType_ValidBedType() throws Exception {
		// Arrange
		String validBedType = "USUAL_BED";

		// Mock
		List<Bed> bedsByType = new ArrayList<>();
		Bed testBed = new Bed();
		testBed.setId("1");
		testBed.setHospitalId("hospital123");
		testBed.setBedType(Bed.BedType.ICU_BED);
		testBed.setCostPerDay(200.0);
		bedsByType.add(testBed);
		Mockito.when(bedService.getBedsByType(validBedType)).thenReturn(bedsByType);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/beds/findByType/{bedType}", validBedType))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id").value("1")).andExpect(jsonPath("$[0].hospitalId").value("hospital123"))
				.andExpect(jsonPath("$[0].bedType").value("ICU_BED"))
				.andExpect(jsonPath("$[0].costPerDay").value(200.0));
	}

//	@Test
//	void testGetBedsByType_InvalidBedType() throws Exception {
//		// Arrange
//		String invalidBedType = "INVALID_BED"; // This is an invalid bed type
//
//		// Act and Assert
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/beds/findByType/{bedType}", invalidBedType))
//				.andExpect(status().isNotFound());
//	}

	@Test
	public void testGetBedsByHospitalId_ValidHospitalId() throws Exception {
		// Arrange
		String hospitalId = "hospital123";

		// Mock
		List<Bed> beds = new ArrayList<>();
		Bed testBed = new Bed();
		testBed.setId("1");
		testBed.setHospitalId("hospital123");
		testBed.setBedType(Bed.BedType.ICU_BED);
		testBed.setCostPerDay(200.0);
		beds.add(testBed);
		when(bedService.getBedsByHospitalId(hospitalId)).thenReturn(beds);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/api/beds/findByHospital/{hospitalId}", hospitalId))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id").value("1")).andExpect(jsonPath("$[0].hospitalId").value("hospital123"))
				.andExpect(jsonPath("$[0].bedType").value("ICU_BED"))
				.andExpect(jsonPath("$[0].costPerDay").value(200.0));
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

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}