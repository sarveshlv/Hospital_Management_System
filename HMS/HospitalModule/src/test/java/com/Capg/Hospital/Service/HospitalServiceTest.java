package com.Capg.Hospital.Service;

import com.Capg.Hospital.Constants.Address;
import com.Capg.Hospital.Constants.HospitalType;
import com.Capg.Hospital.DTO.AddHospitalDTO;
import com.Capg.Hospital.DTO.UpdateHospitalDTO;
import com.Capg.Hospital.Exceptions.HospitalNotFoundException;
import com.Capg.Hospital.Model.Hospital;
import com.Capg.Hospital.Repository.HospitalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class HospitalServiceTest {

    @Mock
    private HospitalRepository hospitalRepository;
    @InjectMocks
    private HospitalServiceImpl hospitalService;


    @Test
    public void testRegisterHospital() {
        AddHospitalDTO hospitalDTO = createAddHospitalRequest();

        Hospital hospital = createHospital();

        when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospital);

        Hospital registeredHospital = hospitalService.registerHospital(hospitalDTO);

        assertNotNull(registeredHospital);
        assertEquals(registeredHospital.getHospitalName(), hospitalDTO.getHospitalName());
        assertEquals(registeredHospital.getHospitalAddress(), hospitalDTO.getHospitalAddress());

        verify(hospitalRepository, times(1)).save(any(Hospital.class));
    }

    @Test
    public void testUpdateHospital() throws HospitalNotFoundException {
        String hospitalId = "abcd1234";
        UpdateHospitalDTO updateHospitalDTO = createUpdateHospitalDTO();

        Hospital existingHospital = createHospital();

        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(existingHospital));
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(existingHospital);

        Hospital updatedHospital = hospitalService.updateHospital(hospitalId, updateHospitalDTO);

        assertNotNull(updatedHospital);
        assertEquals(updatedHospital.getHospitalName(), existingHospital.getHospitalName());

        verify(hospitalRepository, times(1)).findById(hospitalId);
        verify(hospitalRepository, times(1)).save(any(Hospital.class));
    }
//
    @Test
    public void testVerifyHospital() throws HospitalNotFoundException {
        String hospitalId = "abcd123"; // Replace with an actual hospital ID
        String status = "APPROVE"; // Replace with an actual status

        Hospital hospital = createHospital();

        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(hospital));
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospital);

        Hospital verifiedHospital =hospitalService.verifyHospital(hospital.getHospitalId(), status);
        assertNotNull(verifiedHospital);
        assertEquals(verifiedHospital.getStatus(), "APPROVED");

        verify(hospitalRepository, times(1)).findById(hospitalId);
    }
//
    @Test
    public void testFindNearbyHospitals() {
        Long pincode = 560097L; // Replace with an actual pincode
        List<Hospital> hospitals = new ArrayList<>();
        Hospital obj = createHospital();
        hospitals.add(obj);

        when(hospitalRepository.getHospitalByPincode(pincode - 1, pincode + 1)).thenReturn(hospitals);

        List<Hospital> nearbyHospitals = hospitalService.findNearbyHospitals(pincode);

        assertNotNull(nearbyHospitals);

        verify(hospitalRepository, times(1)).getHospitalByPincode(pincode - 1, pincode + 1);
    }
//
    @Test
    public void testFindHospitalById() {
        String hospitalId = "abcd123"; // Replace with an actual hospital ID
        Hospital hospital = createHospital();

        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(hospital));

        Hospital foundHospital = hospitalService.findHospitalById(hospitalId);

        assertNotNull(foundHospital);

        verify(hospitalRepository, times(1)).findById(hospitalId);
    }

    @Test
    public void testDeleteHospitalById() {
        String hospitalId = "abcd1234"; // Replace with an actual hospital ID
        Hospital hospital = createHospital();

        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(hospital));

        Hospital deletedHospital = hospitalService.deleteHospitalById(hospitalId);

        assertNotNull(deletedHospital);

        verify(hospitalRepository, times(1)).findById(hospitalId);
        verify(hospitalRepository, times(1)).deleteById(hospitalId);
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
