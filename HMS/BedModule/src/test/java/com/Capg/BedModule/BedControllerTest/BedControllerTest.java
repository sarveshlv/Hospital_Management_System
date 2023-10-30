package com.Capg.BedModule.BedControllerTest;

import com.Capg.BedModule.Constants.BedStatus;
import com.Capg.BedModule.Constants.BedType;
import com.Capg.BedModule.Controller.BedController;
import com.Capg.BedModule.DTO.AddBedDTO;
import com.Capg.BedModule.Exceptions.CentralExceptionHandler;
import com.Capg.BedModule.Model.Bed;
import com.Capg.BedModule.Service.BedService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BedService bedService;

    @InjectMocks
    private BedController bedController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bedController).setControllerAdvice(new CentralExceptionHandler())
                .build();
    }

    @Test
    public void testRegisterBed() throws Exception {
        String authorizationHeader = "Bearer accessToken";
        AddBedDTO addBedDTO = createAddBedDTO();

        Bed bed = createBed();

        when(bedService.registerBed(anyString(), any(AddBedDTO.class))).thenReturn(bed);

        mockMvc.perform(post("/Bed/register")
                        .header("Authorization", authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addBedDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bedId").value(bed.getBedId()));

        verify(bedService, times(1)).registerBed(anyString(), any(AddBedDTO.class));
    }

    @Test
    public void testUpdateBedDetails() throws Exception {
        String bedId = "bed123";
        Double cost = 1000.0;

        Bed bed = createBed();

        when(bedService.updateBedDetails(bedId, cost)).thenReturn(bed);

        mockMvc.perform(put("/Bed/updateBed/{bedId}/{cost}", bedId, cost))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bedId").value(bed.getBedId()));

        verify(bedService, times(1)).updateBedDetails(bedId, cost);
    }
//
    @Test
    public void testGetAllBeds() throws Exception {
        List<Bed> beds = new ArrayList<>();
        Bed bed = createBed();
        beds.add(bed);

        when(bedService.getAllBeds()).thenReturn(beds);

        mockMvc.perform(get("/Bed/getAllBeds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].bedId").value(beds.get(0).getBedId()));

        verify(bedService, times(1)).getAllBeds();
    }
//
    @Test
    public void testGetBedsByHospitalId() throws Exception {
        String hospitalId = "abcd1234";
        List<Bed> beds = new ArrayList<>();
        Bed bed = createBed();
        beds.add(bed);

        when(bedService.getBedsByHospitalId(hospitalId)).thenReturn(beds);

        mockMvc.perform(get("/Bed/getByHospital/{hospitalId}", hospitalId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].hospitalId").value(beds.get(0).getHospitalId()));

        verify(bedService, times(1)).getBedsByHospitalId(hospitalId);
    }
//
    @Test
    public void testGetNearByBeds() throws Exception {
        String authorizationHeader = "Bearer accessToken";
        Long pincode = 560097L;
        List<Bed> beds = new ArrayList<>();
        Bed bed = createBed();
        beds.add(bed);

        when(bedService.getNearByBeds(authorizationHeader, pincode)).thenReturn(beds);

        mockMvc.perform(get("/Bed/getNearByBeds/{pincode}", pincode)
                        .header("Authorization", authorizationHeader))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].bedId").value(beds.get(0).getBedId()));

        verify(bedService, times(1)).getNearByBeds(authorizationHeader, pincode);
    }
//
    @Test
    public void testGetBedsByType() throws Exception {
        String bedType = "REGULAR_BED";

        List<Bed> beds = new ArrayList<>();
        Bed bed = createBed();
        beds.add(bed);

        when(bedService.getBedsByType(bedType)).thenReturn(beds);

        mockMvc.perform(get("/Bed/getByType/{bedType}", bedType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].bedId").value(beds.get(0).getBedId()));

        verify(bedService, times(1)).getBedsByType(bedType);
    }
//
    @Test
    public void testBookBed() throws Exception {
        String bedId = "bed123";

        Bed bed = createBed();

        when(bedService.bookBed(bedId)).thenReturn(bed);

        mockMvc.perform(put("/Bed/bookBed/{bedId}", bedId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bedId").value(bed.getBedId()));

        verify(bedService, times(1)).bookBed(bedId);
    }
//
    @Test
    public void testMakeBedAvailable() throws Exception {
        String bedId = "bed123";

        Bed bed = createBed();

        when(bedService.makeBedAvailable(bedId)).thenReturn(bed);

        mockMvc.perform(put("/Bed/makeBedAvailable/{bedId}", bedId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bedId").value(bed.getBedId()));

        verify(bedService, times(1)).makeBedAvailable(bedId);
    }
//
    @Test
    public void testDeleteBed() throws Exception {
        String bedId = "bedId";

        Bed bed = createBed();

        when(bedService.deleteBed(bedId)).thenReturn(bed);

        mockMvc.perform(delete("/Bed/deleteBed/{bedId}", bedId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bedId").value(bed.getBedId()));

        verify(bedService, times(1)).deleteBed(bedId);
    }

    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private static AddBedDTO createAddBedDTO() {
        AddBedDTO obj = new AddBedDTO();
        obj.setHospitalId("abcd1234");
        obj.setBedType(String.valueOf(BedType.REGULAR_BED));
        obj.setCostPerDay(1000.0);
        return obj;
    }

    private static Bed createBed() {
        Bed obj = new Bed();
        obj.setBedId("bed123");
        obj.setHospitalId("abcd1234");
        obj.setBedType(BedType.REGULAR_BED);
        obj.setBedStatus(BedStatus.AVAILABLE);
        obj.setCostPerDay(1000.0);
        return obj;
    }

}
