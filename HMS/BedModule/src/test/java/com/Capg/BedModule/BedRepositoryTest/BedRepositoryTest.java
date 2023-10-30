package com.Capg.BedModule.BedRepositoryTest;

import com.Capg.BedModule.Constants.BedStatus;
import com.Capg.BedModule.Constants.BedType;
import com.Capg.BedModule.DTO.AddBedDTO;
import com.Capg.BedModule.Model.Bed;
import com.Capg.BedModule.Repository.BedRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BedRepositoryTest {

    @Mock
    private BedRepository bedRepository;


    @Test
    public void testFindByHospitalId() {
        String hospitalId = "abcd1234";

        List<Bed> expectedBeds = new ArrayList<>();
        Bed bed1 = createBed();
        expectedBeds.add(bed1);

        when(bedRepository.findByHospitalId(hospitalId)).thenReturn(expectedBeds);

        List<Bed> result = bedRepository.findByHospitalId(hospitalId);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(bedRepository, times(1)).findByHospitalId(eq(hospitalId));
    }

    @Test
    public void testFindByBedType() {
        String bedType = "REGULAR_BED";

        List<Bed> expectedBeds = new ArrayList<>();
        Bed bed1 = createBed();
        expectedBeds.add(bed1);

        when(bedRepository.findByBedType(eq(bedType))).thenReturn(expectedBeds);

        List<Bed> result = bedRepository.findByBedType(bedType);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(bedRepository, times(1)).findByBedType(eq(bedType));
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
