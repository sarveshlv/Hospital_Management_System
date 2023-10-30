package com.Capg.Patient.PatientServiceTest;

import com.Capg.Patient.Constants.Address;
import com.Capg.Patient.DTO.AddPatientDTO;
import com.Capg.Patient.Exception.PatientNotFoundException;
import com.Capg.Patient.Model.Patient;
import com.Capg.Patient.Repository.PatientRepository;
import com.Capg.Patient.Service.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PatientServiceTest {

    @InjectMocks
    private PatientServiceImpl patientService;

    @Mock
    private PatientRepository patientRepository;

    @Test
    public void testAddPatient() {
        AddPatientDTO addPatientDTO = createAddPatientDTO();
        Patient patient = createPatient();

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient addedPatient = patientService.addPatient(addPatientDTO);

        assertNotNull(addedPatient);
        assertEquals(addedPatient.getFirstName(), addPatientDTO.getFirstName());
        assertEquals(addedPatient.getLastName(), addPatientDTO.getLastName());

        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    public void testUpdatePatient() throws PatientNotFoundException {
        String patientId = "abcd1234"; // Replace with an actual patient ID
        AddPatientDTO addPatientDTO = createAddPatientDTO();

        Patient existingPatient = createPatient();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(existingPatient); // Return the updated patient

        Patient updatedPatient = patientService.updatePatient(patientId, addPatientDTO);

        assertNotNull(updatedPatient);
        assertEquals(updatedPatient.getFirstName(), addPatientDTO.getFirstName());
        assertEquals(updatedPatient.getLastName(), addPatientDTO.getLastName());

        verify(patientRepository, times(1)).findById(patientId);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    public void testFindPatientById() {
        String patientId = "abcd1234"; // Replace with an actual patient ID
        Patient patient = createPatient();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        Patient foundPatient = patientService.findPatientById(patientId);

        assertNotNull(foundPatient);

        verify(patientRepository, times(1)).findById(patientId);
    }

    private static Patient createPatient() {
        Patient obj = new Patient();
        obj.setPatientId("abcd1234");
        obj.setFirstName("Sarvesh");
        obj.setLastName("LV");
        obj.setContactNumber(9482729175L);
        obj.setAadharCard(928243121710L);
        obj.setAddress(createAddress());
        obj.setPincode(560097L);
        return obj;
    }

    private static Address createAddress() {
        Address obj = new Address();
        obj.setStreetAddress("Basavasamithi LT");
        obj.setCityName("Bangalore");
        obj.setStateName("Karnataka");
        return obj;
    }

    private static AddPatientDTO createAddPatientDTO() {
        AddPatientDTO obj = new AddPatientDTO();
        obj.setFirstName("Sarvesh");
        obj.setLastName("LV");
        obj.setContactNumber(9482729175L);
        obj.setAadharNumber(928243121710L);
        obj.setAddress(createAddress());
        obj.setPincode(560097L);
        return obj;
    }
}
