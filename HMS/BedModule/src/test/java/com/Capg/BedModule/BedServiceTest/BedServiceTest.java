package com.Capg.BedModule.BedServiceTest;

import com.Capg.BedModule.Constants.BedStatus;
import com.Capg.BedModule.Constants.BedType;
import com.Capg.BedModule.DTO.AddBedDTO;
import com.Capg.BedModule.DTO.Hospital;
import com.Capg.BedModule.FeignClients.HospitalServiceClient;
import com.Capg.BedModule.Model.Bed;
import com.Capg.BedModule.Repository.BedRepository;
import com.Capg.BedModule.Service.BedServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BedServiceTest {

    @InjectMocks
    private BedServiceImpl bedService;

    @Mock
    private BedRepository bedRepository;

    @Mock
    private HospitalServiceClient hospitalServiceClient;

    @Test
    public void testRegisterBed() {
        String authorizationHeader = "Bearer accessToken";
        AddBedDTO addBedDTO = createAddBedDTO();

        Hospital hospital = new Hospital();
        hospital.setHospitalId("abcd1234");

        Bed bed = createBed();

        when(hospitalServiceClient.isHospitalFound(eq(authorizationHeader), eq(addBedDTO.getHospitalId())))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        when(bedRepository.save(any(Bed.class))).thenReturn(bed);

        Bed result = bedService.registerBed(authorizationHeader, addBedDTO);

        assertNotNull(result);
        assertEquals(addBedDTO.getHospitalId(), result.getHospitalId());
        assertEquals(BedType.REGULAR_BED, result.getBedType());
        assertEquals(BedStatus.AVAILABLE, result.getBedStatus());
        assertEquals(addBedDTO.getCostPerDay(), result.getCostPerDay());

        verify(hospitalServiceClient, times(1)).isHospitalFound(eq(authorizationHeader), eq(addBedDTO.getHospitalId()));
        verify(bedRepository, times(1)).save(any(Bed.class));
    }

    @Test
    public void testUpdateBedDetails() {
        String bedId = "bed123";
        Double cost = 1000.0;

        Bed bed = createBed();
        bed.setCostPerDay(50.0);

        when(bedRepository.findById(eq(bedId))).thenReturn(Optional.of(bed));
        when(bedRepository.save(any(Bed.class))).thenReturn(bed);

        Bed result = bedService.updateBedDetails(bedId, cost);

        assertNotNull(result);
        assertEquals(cost, result.getCostPerDay());

        verify(bedRepository, times(1)).findById(eq(bedId));
        verify(bedRepository, times(1)).save(any(Bed.class));
    }

    @Test
    public void testGetAllBeds() {
        List<Bed> beds = new ArrayList<>();
        Bed bed1 = createBed();
        beds.add(bed1);

        when(bedRepository.findAll()).thenReturn(beds);

        List<Bed> result = bedService.getAllBeds();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(bedRepository, times(1)).findAll();
    }

    @Test
    public void testGetBedsByHospitalId() {
        String hospitalId = "abcd1234";
        List<Bed> beds = new ArrayList<>();
        Bed bed1 = createBed();
        beds.add(bed1);


        when(bedRepository.findByHospitalId(eq(hospitalId))).thenReturn(beds);

        List<Bed> result = bedService.getBedsByHospitalId(hospitalId);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(bedRepository, times(1)).findByHospitalId(eq(hospitalId));
    }

    @Test
    public void testGetNearByBeds() {
        String authorizationHeader = "Bearer yourAccessToken";
        Long pincode = 560097L;

        Hospital hospital1 = new Hospital();
        hospital1.setHospitalId("abcd1234");

        List<Hospital> nearbyHospitals = new ArrayList<>();
        nearbyHospitals.add(hospital1);

        Bed bed1 = createBed();

        when(hospitalServiceClient.getNearbyHospitals(eq(authorizationHeader), eq(pincode))).thenReturn(nearbyHospitals);
        when(bedRepository.findByHospitalId(eq("abcd1234"))).thenReturn(Collections.singletonList(bed1));

        List<Bed> result = bedService.getNearByBeds(authorizationHeader, pincode);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(hospitalServiceClient, times(1)).getNearbyHospitals(eq(authorizationHeader), eq(pincode));
        verify(bedRepository, times(1)).findByHospitalId(eq("abcd1234"));
    }

    @Test
    public void testGetBedsByType() {
        String bedType = "REGULAR_BED";

        List<Bed> beds = new ArrayList<>();
        Bed bed1 = new Bed();
        beds.add(bed1);

        when(bedRepository.findByBedType(eq(bedType))).thenReturn(beds);

        List<Bed> result = bedService.getBedsByType(bedType);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(bedRepository, times(1)).findByBedType(eq(bedType));
    }

    @Test
    public void testBookBed() {
        String bedId = "bed123";

        Bed bed = createBed();

        when(bedRepository.findById(eq(bedId))).thenReturn(Optional.of(bed));
        when(bedRepository.save(any(Bed.class))).thenReturn(bed);

        Bed result = bedService.bookBed(bedId);

        assertNotNull(result);
        assertEquals(BedStatus.BOOKED, result.getBedStatus());

        verify(bedRepository, times(1)).findById(eq(bedId));
        verify(bedRepository, times(1)).save(any(Bed.class));
    }

    @Test
    public void testMakeBedAvailable() {
        String bedId = "bed123";

        Bed bed = createBed();

        when(bedRepository.findById(eq(bedId))).thenReturn(Optional.of(bed));
        when(bedRepository.save(any(Bed.class))).thenReturn(bed);

        Bed result = bedService.makeBedAvailable(bedId);

        assertNotNull(result);
        assertEquals(BedStatus.AVAILABLE, result.getBedStatus());

        verify(bedRepository, times(1)).findById(eq(bedId));
        verify(bedRepository, times(1)).save(any(Bed.class));
    }

    @Test
    public void testDeleteBed() {
        String bedId = "bed123";
        Bed bed = createBed();

        when(bedRepository.findById(eq(bedId))).thenReturn(Optional.of(bed));

        Bed result = bedService.deleteBed(bedId);

        assertNotNull(result);
        assertEquals(BedStatus.AVAILABLE, result.getBedStatus());

        verify(bedRepository, times(1)).findById(eq(bedId));
        verify(bedRepository, times(1)).deleteById(eq(bedId));
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
