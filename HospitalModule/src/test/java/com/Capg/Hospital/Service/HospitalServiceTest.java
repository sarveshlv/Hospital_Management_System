//package com.Capg.Hospital.Service;
//
//import com.Capg.Hospital.Constants.HospitalType;
//import com.Capg.Hospital.DTO.AddHospitalDTO;
//import com.Capg.Hospital.Exceptions.HospitalNotFoundException;
//import com.Capg.Hospital.Model.Hospital;
//import com.Capg.Hospital.Repository.HospitalRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class HospitalServiceTest {
//    @Mock
//    private HospitalRepository hospitalRepository;
//    @InjectMocks
//    private HospitalServiceImpl hospitalService;
//
//    @BeforeEach
//    void setUp() {
//        // Initialize Mockito mocks before each test method.
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testRegisterHospital() {
//        AddHospitalDTO hospitalDTO = createAddHospitalRequest();
//        Hospital expectedHospital = createHospital();
//        when(hospitalRepository.save(any(Hospital.class))).thenReturn(expectedHospital);
//        Hospital savedHospital = hospitalService.registerHospital(hospitalDTO);
//        assertEquals(expectedHospital, savedHospital);
//    }
//
//    @Test
//    public void testVerifyHospitalApprove() {
//        String hospitalId = "Apollo560097";
//        String status = "APPROVE";
//        Hospital existingHospital = createHospital();
//        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(existingHospital));
//        Hospital approvedHospital = hospitalService.verifyHospital(hospitalId, status);
//        assertEquals("APPROVED", approvedHospital.getStatus());
//        verify(hospitalRepository, times(1)).save(existingHospital);
//    }
//
//    @Test
//    public void testVerifyHospitalReject() {
//        String hospitalId = "TestHospital12345";
//        String status = "REJECT";
//
//        Hospital existingHospital = new Hospital();
//        existingHospital.setHospitalId(hospitalId);
//        existingHospital.setStatus("Requested");
//
//        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(existingHospital));
//
//        Hospital rejectedHospital = hospitalService.verifyHospital(hospitalId, status);
//
//        assertEquals("Requested", rejectedHospital.getStatus());
//        assertNull(rejectedHospital);
//
//        verify(hospitalRepository, never()).save(any(Hospital.class));
//    }
//
//    @Test
//    public void testVerifyHospitalHospitalNotFound() {
//
//        String hospitalId = "NonExistentHospital";
//        String status = "APPROVE";
//        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.empty());
//        hospitalService.verifyHospital(hospitalId, status);
//    }
//
//    @Test
//    public void testFindNearbyHospitals() {
//
//        Long pincode = 12345L;
//        List<Hospital> mockHospitalList = new ArrayList<>();
//        Hospital obj1 = createHospital();
//        obj1.setPincode(560096L);
//        Hospital obj2 = createHospital();
//        obj2.setPincode(560098L);
//        mockHospitalList.add(obj1);
//        mockHospitalList.add(obj2);
//
//        when(hospitalRepository.getHospitalByPincode(pincode - 1, pincode + 1)).thenReturn(mockHospitalList);
//
//        List<Hospital> result = hospitalService.findNearbyHospitals(pincode);
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        assertEquals(560096, result.get(0).getPincode());
//        assertEquals(560098, result.get(1).getPincode());
//
//        verify(hospitalRepository, times(1)).getHospitalByPincode(pincode - 1, pincode + 1);
//    }
//
//    @Test
//    public void testFindNearbyHospitalsNoHospitalsFound() {
//        Long pincode = 560081L;
//        when(hospitalRepository.getHospitalByPincode(pincode - 1, pincode + 1)).thenReturn(new ArrayList<>());
//        assertThrows(HospitalNotFoundException.class, () -> hospitalService.findNearbyHospitals(pincode));
//        verify(hospitalRepository, times(1)).getHospitalByPincode(pincode - 1, pincode + 1);
//    }
//
//    @Test
//    public void testFindHospitalById() {
//        String hospitalId = "Apollo560097";
//        Hospital mockHospital = createHospital();
//        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(mockHospital));
//        Hospital result = hospitalService.findHospitalById(hospitalId);
//        assertNotNull(result);
//        assertEquals("Apollo", result.getHospitalName());
//        verify(hospitalRepository, times(1)).findById(hospitalId);
//    }
//
//    @Test
//    public void testFindHospitalByIdNotFound() {
//
//        String hospitalId = "456";
//        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.empty());
//        assertThrows(HospitalNotFoundException.class, () -> hospitalService.findHospitalById(hospitalId));
//        verify(hospitalRepository, times(1)).findById(hospitalId);
//    }
//
//    @Test
//    public void testDeleteHospitalById() {
//        // Prepare test data
//        String hospitalId = "Apollo560097";
//        Hospital mockHospital = createHospital();
//        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(mockHospital));
//        Hospital deletedHospital = hospitalService.deleteHospitalById(hospitalId);
//        assertEquals(mockHospital, deletedHospital);
//        verify(hospitalRepository, times(1)).deleteById(hospitalId);
//    }
//    @Test
//    void testFindHospitalByIdHospitalNotFound() {
//
//        String hospitalId = "1";
//        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.empty());
//        assertThrows(HospitalNotFoundException.class, () -> hospitalService.findHospitalById(hospitalId));
//        verify(hospitalRepository, times(1)).findById(hospitalId);
//    }
//
//
//
//    private static AddHospitalDTO createAddHospitalRequest() {
//        AddHospitalDTO request = new AddHospitalDTO();
//        request.setHospitalName("Apollo");
//        request.setHospitalAddress("Yeshtwantpur, Bangalore");
//        request.setPincode(560097L);
//        request.setHospitalType("PRIVATE");
//
//        return request;
//    }
//
//    private static Hospital createHospital() {
//        Hospital hospital = new Hospital();
//        hospital.setHospitalId("Apollo560097");
//        hospital.setHospitalName("Apollo");
//        hospital.setHospitalType(HospitalType.PRIVATE);
//        hospital.setHospitalAddress("Yeshtwantpur, Bangalore");
//        hospital.setPincode(560097L);
//        hospital.setStatus("Requested");
//        return hospital;
//    }
//}
