package com.Capg.Hospital.Controller;

import com.Capg.Hospital.Constants.Address;
import com.Capg.Hospital.Constants.HospitalType;
import com.Capg.Hospital.DTO.AddHospitalDTO;
import com.Capg.Hospital.DTO.UpdateHospitalDTO;
import com.Capg.Hospital.Exceptions.CentralExceptionHandler;
import com.Capg.Hospital.Exceptions.HospitalNotFoundException;
import com.Capg.Hospital.Model.Hospital;
import com.Capg.Hospital.Service.HospitalService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HospitalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HospitalService hospitalService;

    @InjectMocks
    private HospitalController hospitalController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(hospitalController).setControllerAdvice(new CentralExceptionHandler())
                .build();
    }
    @Test
    public void testRegisterHospital() throws Exception {
        AddHospitalDTO hospitalDTO = createAddHospitalRequest();

        Hospital hospital = createHospital();

        when(hospitalService.registerHospital(any(AddHospitalDTO.class))).thenReturn(hospital);

        mockMvc.perform(post("/Hospital/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(hospitalDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.hospitalName").value(hospital.getHospitalName()));

        verify(hospitalService, times(1)).registerHospital(any(AddHospitalDTO.class));
    }

    @Test
    public void testGetAllHospitals() throws Exception {
        List<Hospital> hospitals = new ArrayList<>();
        Hospital obj = createHospital();
        hospitals.add(obj);

        when(hospitalService.getAllHospitals()).thenReturn(hospitals);

        mockMvc.perform(get("/Hospital/getHospitals"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].hospitalName").value(hospitals.get(0).getHospitalName()));

        verify(hospitalService, times(1)).getAllHospitals();
    }
//
    @Test
    public void testUpdateHospital() throws Exception {
        String hospitalId = "abcd1234";
        UpdateHospitalDTO updateHospitalDTO = createUpdateHospitalDTO();

        Hospital hospital = createHospital();

        when(hospitalService.updateHospital(eq(hospitalId), any(UpdateHospitalDTO.class))).thenReturn(hospital);

        mockMvc.perform(put("/Hospital/updateHospital/{id}", hospitalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateHospitalDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.hospitalName").value(hospital.getHospitalName()));

        verify(hospitalService, times(1)).updateHospital(eq(hospitalId), any(UpdateHospitalDTO.class));
    }
//
    @Test
    public void testApproveDeclineRegistration() throws Exception {
        String hospitalId = "abcd1234";
        String status = "APPROVE";

        Hospital hospital = createHospital();

        when(hospitalService.verifyHospital(eq(hospitalId), eq(status))).thenReturn(hospital);

        mockMvc.perform(put("/Hospital/verifyRegistration/{hospitalId}", hospitalId)
                        .param("status", status))
                .andExpect(status().isOk()) // You can change the expected status code as needed
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(hospital.getStatus()));

        verify(hospitalService, times(1)).verifyHospital(eq(hospitalId), eq(status));
    }
//
    @Test
    public void testFindNearbyHospitals() throws Exception {
        Long pincode = 560097L;
        List<Hospital> hospitals = new ArrayList<>();
        Hospital obj = createHospital();
        hospitals.add(obj);

        when(hospitalService.findNearbyHospitals(eq(pincode))).thenReturn(hospitals);

        mockMvc.perform(get("/Hospital/findNearbyHospitals/{pincode}", pincode))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].pincode").value(hospitals.get(0).getPincode()));

        verify(hospitalService, times(1)).findNearbyHospitals(eq(pincode));
    }
//
    @Test
    public void testGetHospitalById() throws Exception {
        String hospitalId = "abcd1234"; // Replace with an actual hospital ID
        Hospital hospital = createHospital();

        when(hospitalService.findHospitalById(eq(hospitalId))).thenReturn(hospital);

        mockMvc.perform(get("/Hospital/getHospital/{hospitalId}", hospitalId))
                .andExpect(status().isOk()) // You can change the expected status code as needed
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.hospitalName").value(hospital.getHospitalName()));

        verify(hospitalService, times(1)).findHospitalById(eq(hospitalId));
    }
//
    @Test
    public void testDeleteHospitalById() throws Exception {
        String hospitalId = "abcd1234"; // Replace with an actual hospital ID
        Hospital hospital = createHospital();

        when(hospitalService.deleteHospitalById(eq(hospitalId))).thenReturn(hospital);

        mockMvc.perform(delete("/Hospital/deleteHospital/{hospitalId}", hospitalId))
                .andExpect(status().isOk()) // You can change the expected status code as needed
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.hospitalName").value(hospital.getHospitalName()));

        verify(hospitalService, times(1)).deleteHospitalById(eq(hospitalId));
    }

    // Utility method to convert object to JSON string
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }



    private static AddHospitalDTO createAddHospitalRequest() {
        AddHospitalDTO request = new AddHospitalDTO();
        request.setHospitalName("Apollo");
        request.setHospitalAddress(createAddress());
        request.setPincode(560097L);
        request.setHospitalType("PRIVATE");

        return request;
    }

    private static UpdateHospitalDTO createUpdateHospitalDTO() {
        UpdateHospitalDTO request = new UpdateHospitalDTO();
        request.setContactNo(9482729715L);
        request.setHospitalAddress(createAddress());
        request.setPincode(560097L);
        return request;
    }

    private static Hospital createHospital() {
        Hospital hospital = new Hospital();
        hospital.setHospitalId("abcd123");
        hospital.setHospitalName("Apollo");
        hospital.setHospitalType(HospitalType.PRIVATE);
        hospital.setHospitalAddress(createAddress());
        hospital.setPincode(560097L);
        hospital.setStatus("APPROVED");
        hospital.setContactNo(9482729715L);
        return hospital;
    }

    private static Address createAddress() {
        Address obj = new Address();
        obj.setStreetAddress("Basavasamithi LT");
        obj.setCityName("Bangalore");
        obj.setStateName("Karnataka");
        return obj;
    }
}
