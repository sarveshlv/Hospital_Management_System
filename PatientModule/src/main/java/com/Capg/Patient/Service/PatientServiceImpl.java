package com.Capg.Patient.Service;

import com.Capg.Patient.DTO.AddPatientDTO;
import com.Capg.Patient.Exception.PatientNotFoundException;
import com.Capg.Patient.Model.Patient;
import com.Capg.Patient.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService{
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Patient addPatient(AddPatientDTO addPatientDTO) {
        Patient patient = new Patient();
        patient.setFirstName(addPatientDTO.getFirstName());
        patient.setLastName(addPatientDTO.getLastName());
        patient.setContactNumber(addPatientDTO.getContactNumber());
        patient.setAadharCard(addPatientDTO.getAadharNumber());
        patient.setAddress(addPatientDTO.getAddress());

        patient = patientRepository.save(patient);
//        userApiService.addReference(patient.getId());
        return patient;
    }

    @Override
    public Patient updatePatient(String patientId, AddPatientDTO addPatientDTO) throws PatientNotFoundException {
        Patient patient = findPatientById(patientId);
        patient.setFirstName(addPatientDTO.getFirstName());
        patient.setLastName(addPatientDTO.getLastName());
        patient.setContactNumber(addPatientDTO.getContactNumber());
        patient.setAadharCard(addPatientDTO.getAadharNumber());
        patient.setAddress(addPatientDTO.getAddress());
        return patientRepository.save(patient);
    }

    @Override
    public Patient findPatientById(String patientId) throws PatientNotFoundException {
        return patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException(patientId));
    }
}
