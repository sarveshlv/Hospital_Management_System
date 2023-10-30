package com.Capg.Patient.Service;

import com.Capg.Patient.DTO.AddPatientDTO;
import com.Capg.Patient.Exception.PatientNotFoundException;
import com.Capg.Patient.Model.Patient;

public interface PatientService {
    Patient addPatient(AddPatientDTO addPatientDTO);

    Patient updatePatient(String id, AddPatientDTO addPatientRequest) throws PatientNotFoundException;

    Patient findPatientById(String id) throws PatientNotFoundException;
}
