//package com.Capg.BedModule.Controller;
//
//import com.Capg.BedModule.DTO.AddBedDTO;
//import com.Capg.BedModule.Exceptions.BedNotFoundException;
//import com.Capg.BedModule.Model.Bed;
//import com.Capg.BedModule.Service.BedService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class BedControllerTest {
//
//    @InjectMocks
//    private BedController bedController;
//
//    @Mock
//    private BedService bedService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testRegisterBed() {
//        // Prepare test data
//        AddBedDTO addBedObj = new AddBedDTO();
//        Bed registeredBed = new Bed();
//        when(bedService.registerBed(addBedObj)).thenReturn(registeredBed);
//
//        // Call the controller method
//        ResponseEntity<?> responseEntity = bedController.registerBed(addBedObj);
//
//        // Verify the response status and body
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(registeredBed, responseEntity.getBody());
//    }
//
////    @Test
////    public void testRegisterBedException() {
////        // Prepare test data
////        AddBedDTO addBedObj = new AddBedDTO();
////        when(bedService.registerBed(addBedObj)).thenThrow(new Exception("Bed registration failed."));
////
////        // Call the controller method
////        ResponseEntity<?> responseEntity = bedController.registerBed(addBedObj);
////
////        // Verify the response status and message
////        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
////        assertEquals("Bed registration failed.", responseEntity.getBody());
////    }
//
//    @Test
//    public void testGetAllBeds() {
//        // Prepare test data
//        List<Bed> bedList = new ArrayList<>();
//        when(bedService.getAllBeds()).thenReturn(bedList);
//
//        // Call the controller method
//        List<Bed> result = bedController.getAllBeds();
//
//        // Verify the result
//        assertEquals(bedList, result);
//    }
//
//    @Test
//    public void testGetBedsByHospitalId() {
//        // Prepare test data
//        String hospitalId = "123";
//        List<Bed> bedList = new ArrayList<>();
//        when(bedService.getBedsByHospitalId(hospitalId)).thenReturn(bedList);
//
//        // Call the controller method
//        List<Bed> result = bedController.getBedsByHospitalId(hospitalId);
//
//        // Verify the result
//        assertEquals(bedList, result);
//    }
//
//    @Test
//    public void testGetNearByBeds() {
//        // Prepare test data
//        Long pincode = 123456L;
//        List<Bed> bedList = new ArrayList<>();
//        when(bedService.getNearByBeds(pincode)).thenReturn(bedList);
//
//        // Call the controller method
//        List<Bed> result = bedController.getNearByBeds(pincode);
//
//        // Verify the result
//        assertEquals(bedList, result);
//    }
//
//    @Test
//    public void testGetBedsByType() {
//        // Prepare test data
//        String bedType = "REGULAR_BED";
//        List<Bed> bedList = new ArrayList<>();
//        when(bedService.getBedsByType(bedType)).thenReturn(bedList);
//
//        // Call the controller method
//        List<Bed> result = bedController.getBedsByType(bedType);
//
//        // Verify the result
//        assertEquals(bedList, result);
//    }
//
//    @Test
//    public void testBookBed() throws BedNotFoundException {
//        // Prepare test data
//        String bedId = "456";
//        Bed bookedBed = new Bed();
//        when(bedService.bookBed(bedId)).thenReturn(bookedBed);
//
//        // Call the controller method
//        Bed result = bedController.bookBed(bedId);
//
//        // Verify the result
//        assertEquals(bookedBed, result);
//    }
//
//    @Test
//    public void testMakeBedAvailable() throws BedNotFoundException {
//        // Prepare test data
//        String bedId = "789";
//        Bed availableBed = new Bed();
//        when(bedService.makeBedAvailable(bedId)).thenReturn(availableBed);
//
//        // Call the controller method
//        Bed result = bedController.makeBedAvailable(bedId);
//
//        // Verify the result
//        assertEquals(availableBed, result);
//    }
//}
