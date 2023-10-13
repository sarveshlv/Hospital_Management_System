package com.hms.bedms.repository;

import com.hms.bedms.entities.Bed;
import com.hms.bedms.entities.Bed.BedType;
import com.hms.bedms.entities.Bed.BedStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@DataMongoTest
public class BedRepositoryTests {

    @Autowired
    private IBedRepository bedRepository;

    @MockBean
    private IBedRepository bedRepositoryMock;

    // Test case for finding beds by hospital ID (asserts the number of beds)
    @Test
    public void testFindBedsByHospitalId() {
        // Arrange
        Bed bed1 = new Bed();
        bed1.setId("1");
        bed1.setHospitalId("123");
        bed1.setBedType(BedType.USUAL_BED);
        bed1.setBedStatus(BedStatus.AVAILABLE);
        bed1.setCostPerDay(100.0);

        Bed bed2 = new Bed();
        bed2.setId("2");
        bed2.setHospitalId("123");
        bed2.setBedType(BedType.ICU_BED);
        bed2.setBedStatus(BedStatus.BOOKED);
        bed2.setCostPerDay(200.0);

        Bed bed3 = new Bed();
        bed3.setId("3");
        bed3.setHospitalId("456");
        bed3.setBedType(BedType.USUAL_BED);
        bed3.setBedStatus(BedStatus.AVAILABLE);
        bed3.setCostPerDay(150.0);

        List<Bed> bedsForHospital123 = List.of(bed1, bed2);

        // Mock
        Mockito.when(bedRepositoryMock.findByHospitalId(anyString())).thenReturn(bedsForHospital123);

        // Act
        List<Bed> actualBeds = bedRepository.findByHospitalId("123");

        // Assert
        assertEquals(2, actualBeds.size());
    }

    // Test case for finding beds by bed type (asserts the number of beds)
    @Test
    public void testFindBedsByBedType() {
        // Arrange
        Bed bed1 = new Bed();
        bed1.setId("1");
        bed1.setBedType(BedType.USUAL_BED);
        bed1.setBedStatus(BedStatus.AVAILABLE);
        bed1.setCostPerDay(100.0);

        Bed bed2 = new Bed();
        bed2.setId("2");
        bed2.setBedType(BedType.ICU_BED);
        bed2.setBedStatus(BedStatus.BOOKED);
        bed2.setCostPerDay(200.0);

        Bed bed3 = new Bed();
        bed3.setId("3");
        bed3.setBedType(BedType.USUAL_BED);
        bed3.setBedStatus(BedStatus.AVAILABLE);
        bed3.setCostPerDay(150.0);

        List<Bed> usualBeds = List.of(bed1, bed3);

        // Mock
        Mockito.when(bedRepositoryMock.findByBedType("USUAL_BED")).thenReturn(usualBeds);

        // Act
        List<Bed> actualBeds = bedRepository.findByBedType("USUAL_BED");

        // Assert
        assertEquals(2, actualBeds.size());
    }
}