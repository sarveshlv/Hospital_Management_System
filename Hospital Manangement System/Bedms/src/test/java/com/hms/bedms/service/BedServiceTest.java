package com.hms.bedms.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hms.bedms.clients.IHospitalServiceClient;
import com.hms.bedms.dtos.Hospital;
import com.hms.bedms.dtos.UpdateBedRequest;
import com.hms.bedms.entities.Bed;
import com.hms.bedms.exceptions.BedNotFoundException;
import com.hms.bedms.repository.IBedRepository;

@SpringBootTest
public class BedServiceTest {

	@Mock
	private IBedRepository bedRepository;

	@Mock
	private IHospitalServiceClient hospitalServiceClient;
	
	@InjectMocks
	private BedService bedService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	public void testGetNearbyBeds() {
		List<Hospital> nearbyHospitals = new ArrayList<>();
        Hospital hospital1 = createHospital();
        hospital1.setId("1");
        Hospital hospital2 = new Hospital();
        hospital2.setId("2");
        nearbyHospitals.add(hospital1);
        nearbyHospitals.add(hospital2);
        
        List<Bed> bedsForHospital1 = new ArrayList<>();
        Bed bed1 = createBed();
        bed1.setId("1");
        Bed bed2 = createBed();
        bed2.setId("2");
        bedsForHospital1.add(bed1);
        bedsForHospital1.add(bed2);

        List<Bed> bedsForHospital2 = new ArrayList<>();
        Bed bed3 = createBed();
        bed3.setId("3");
        bedsForHospital2.add(bed3);
        
        
        when(hospitalServiceClient.getNearbyHospitals(any(), 226022L)).thenReturn(nearbyHospitals);
        when(bedRepository.findByHospitalId("1")).thenReturn(bedsForHospital1);
        when(bedRepository.findByHospitalId("2")).thenReturn(bedsForHospital2);

        List<Bed> result = bedService.getNearbyBeds(any(), 226022L);

        assertEquals(3, result.size());
        assertTrue(result.contains(bed1));
        assertTrue(result.contains(bed2));
        assertTrue(result.contains(bed3));
    }
	
	@Test
	public void testUpdateBed_ValidBedId() {
		Bed existingBed = createBed();

		when(bedRepository.findById("1")).thenReturn(Optional.of(existingBed));
		when(bedRepository.save(existingBed)).thenReturn(existingBed);

		UpdateBedRequest updateRequest = new UpdateBedRequest("USUAL_BED", 100.0);

		existingBed.setBedType(Bed.BedType.USUAL_BED);
		existingBed.setCostPerDay(100.0);

		assertEquals(existingBed, bedService.updateBed("1", updateRequest));
	}

	@Test
	public void testUpdateBed_BedIdNotFound() {
		UpdateBedRequest updateRequest = new UpdateBedRequest("USUAL_BED", 100.0);

		when(bedRepository.findById("1")).thenReturn(Optional.empty());

		assertThrows(BedNotFoundException.class, () -> bedService.updateBed("1", updateRequest));
	}

	@Test
	public void testFindBedById_ValidBedId() {
		Bed expectedBed = createBed();

		when(bedRepository.findById("1")).thenReturn(Optional.of(expectedBed));

		assertEquals(expectedBed, bedService.findBedById("1"));
	}

	@Test
	public void testFindBedById_BedIdNotFound() {
		when(bedRepository.findById("1")).thenReturn(Optional.empty());

		assertThrows(BedNotFoundException.class, () -> bedService.findBedById("1"));
	}

	@Test
	public void testGetAllBeds() {
		Bed bed1 = createBed();
		bed1.setId("1");

		Bed bed2 = createBed();
		bed2.setId("2");

		List<Bed> allBeds = new ArrayList<>();
		allBeds.add(bed1);
		allBeds.add(bed2);

		when(bedRepository.findAll()).thenReturn(allBeds);

		assertEquals(allBeds, bedService.getAllBeds());
	}

	@Test
	public void testGetBedsByType_ValidBedType() {
		Bed bed = createBed();
		bed.setBedType(Bed.BedType.USUAL_BED);

		List<Bed> expectedBeds = new ArrayList<>();
		expectedBeds.add(bed);

		when(bedRepository.save(bed)).thenReturn(bed);
		when(bedRepository.findByBedType("USUAL_BED")).thenReturn(expectedBeds);

		assertEquals(expectedBeds, bedService.getBedsByType("USUAL_BED"));
	}

	@Test
	public void testGetBedsByType_InvalidBedType() {
		String bedType = "INVALID_BED_TYPE";

		when(bedRepository.findByBedType(bedType)).thenReturn(new ArrayList<>());
		assertTrue(bedService.getBedsByType(bedType).isEmpty());
	}

	@Test
	public void testGetBedsByHospitalId_ValidHospitalId() {
		Bed bed = createBed();

		List<Bed> expectedBeds = new ArrayList<>();
		expectedBeds.add(bed);

		when(bedRepository.save(bed)).thenReturn(bed);
		when(bedRepository.findByHospitalId("1")).thenReturn(expectedBeds);

		assertEquals(expectedBeds, bedService.getBedsByHospitalId("1"));
	}

	@Test
	public void testGetBedsByHospitalId_HospitalIdNotFound() {
		Bed bed = createBed();

		when(bedRepository.save(bed)).thenReturn(bed);

		List<Bed> expectedBeds = new ArrayList<>();
		expectedBeds.add(bed);

		when(bedRepository.findByHospitalId("2")).thenReturn(new ArrayList<>());
		assertTrue(bedService.getBedsByHospitalId("2").isEmpty());
	}

	@Test
	public void testBookBed_ValidBedId() {
		Bed bed = createBed();
		bed.setBedStatus(Bed.BedStatus.AVAILABLE);

		when(bedRepository.findById("1")).thenReturn(Optional.of(bed));
		when(bedRepository.save(bed)).thenReturn(bed);

		assertEquals(Bed.BedStatus.BOOKED, bedService.bookBed("1").getBedStatus());
	}

	@Test
	public void testBookBed_BedIdNotFound() {
		String bedId = "1";

		when(bedRepository.findById(bedId)).thenReturn(Optional.empty());
		assertThrows(BedNotFoundException.class, () -> bedService.bookBed(bedId));
	}

	@Test
	public void testUnbookBed_ValidBedId() {
		Bed bed = createBed();
		bed.setBedStatus(Bed.BedStatus.BOOKED);

		when(bedRepository.findById("1")).thenReturn(Optional.of(bed));
		when(bedRepository.save(bed)).thenReturn(bed);

		assertEquals(Bed.BedStatus.CANCELLED, bedService.unbookBed("1").getBedStatus());
	}

	@Test
	public void testUnbookBed_BedIdNotFound() {
		String bedId = "1";

		when(bedRepository.findById(bedId)).thenReturn(Optional.empty());
		assertThrows(BedNotFoundException.class, () -> bedService.unbookBed(bedId));
	}

	@Test
	public void testMakeBedAvailable_ValidBedId() {
		Bed bed = createBed();
		bed.setBedStatus(Bed.BedStatus.COMPLETED);

		when(bedRepository.findById("1")).thenReturn(Optional.of(bed));
		when(bedRepository.save(bed)).thenReturn(bed);

		assertEquals(Bed.BedStatus.AVAILABLE, bedService.makeBedAvailable("1").getBedStatus());
	}

	@Test
	public void testMakeBedAvailable_BedIdNotFound() {
		String bedId = "1";

		when(bedRepository.findById(bedId)).thenReturn(Optional.empty());
		assertThrows(BedNotFoundException.class, () -> bedService.makeBedAvailable(bedId));
	}

	public static Bed createBed() {
		Bed bed = new Bed();
		bed.setId("1");
		bed.setHospitalId("1");
		bed.setBedType(Bed.BedType.USUAL_BED);
		bed.setBedStatus(Bed.BedStatus.AVAILABLE);
		bed.setCostPerDay(25.0);
		return bed;
	}
	
	private static Hospital createHospital() {
		Hospital hospital = new Hospital();
		hospital.setId("1");
		hospital.setName("Apollo Hospital");
		hospital.setHospitalType(Hospital.HospitalType.PRIVATE.toString());
		hospital.setContactNo(7270043813L);
		hospital.setAddress(new Hospital.Address("City", "Uttar Pradesh", 226022L));
		return hospital;
	}
}