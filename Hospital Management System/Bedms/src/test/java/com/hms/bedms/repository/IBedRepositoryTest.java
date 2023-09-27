package com.hms.bedms.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.hms.bedms.entities.Bed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

@DataMongoTest
public class IBedRepositoryTest {

    @Autowired
    private IBedRepository bedRepository;

    
    @AfterEach
    public void cleanup() {
        bedRepository.deleteAll();
    }
    
    @Test
    public void testFindByHospitalId() {
        Bed bed1 = new Bed();
        bed1.setHospitalId("1");
        bed1.setBedType(Bed.BedType.USUAL_BED);
        bedRepository.save(bed1);

        Bed bed2 = new Bed();
        bed2.setHospitalId("2");
        bed2.setBedType(Bed.BedType.ICU_BED);
        bedRepository.save(bed2);

        List<Bed> bedsForHospital = bedRepository.findByHospitalId("1");

        assertNotNull(bedsForHospital);
        assertEquals(1, bedsForHospital.size());
        assertEquals("1", bedsForHospital.get(0).getHospitalId());
    }

    @Test
    public void testFindByBedType() {
        Bed bed1 = new Bed();
        bed1.setHospitalId("1");
        bed1.setBedType(Bed.BedType.USUAL_BED);
        bedRepository.save(bed1);

        Bed bed2 = new Bed();
        bed2.setHospitalId("2");
        bed2.setBedType(Bed.BedType.ICU_BED);
        bedRepository.save(bed2);

        List<Bed> icuBeds = bedRepository.findByBedType(Bed.BedType.ICU_BED.toString());

        assertNotNull(icuBeds);
        assertEquals(1, icuBeds.size());
        assertEquals(Bed.BedType.ICU_BED, icuBeds.get(0).getBedType());
    }
}