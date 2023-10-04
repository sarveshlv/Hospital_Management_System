package com.hms.bedms.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.hms.bedms.entities.Bed;

@DataMongoTest
public class BedRepositoryTests {

    @Mock
    private IBedRepository bedRepository;

    @BeforeEach
    public void setUp() {
        Bed bed1 = new Bed();
        bed1.setId("1");
        bed1.setHospitalId("hospitalId1");
        bed1.setBedType(Bed.BedType.USUAL_BED);
        
        Bed bed2 = new Bed();
        bed2.setId("2");
        bed2.setHospitalId("hospitalId2");
        bed2.setBedType(Bed.BedType.ICU_BED);

        when(bedRepository.findByHospitalId("hospitalId1")).thenReturn(Arrays.asList(bed1));
        when(bedRepository.findByHospitalId("hospitalId2")).thenReturn(Arrays.asList(bed2));
        when(bedRepository.findByBedType("USUAL_BED")).thenReturn(Arrays.asList(bed1));
        when(bedRepository.findByBedType("ICU_BED")).thenReturn(Arrays.asList(bed2));
    }

    @Test
    public void testFindByHospitalId() {
        List<Bed> beds = bedRepository.findByHospitalId("hospitalId1");
        assertEquals(1, beds.size());
        assertEquals("hospitalId1", beds.get(0).getHospitalId());
    }

    @Test
    public void testFindByBedType() {
        List<Bed> beds = bedRepository.findByBedType("ICU_BED");
        assertEquals(1, beds.size());
        assertEquals(Bed.BedType.ICU_BED, beds.get(0).getBedType());
    }
}