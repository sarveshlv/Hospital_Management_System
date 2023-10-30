package com.Capg.Patient.Controller;

import com.Capg.Patient.DTO.AddPatientDTO;
import com.Capg.Patient.Exception.PatientNotFoundException;
import com.Capg.Patient.Model.Patient;
import com.Capg.Patient.Service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Patient")
public class PatientController {
    @Autowired
    private PatientService patientService;

    //Register patient into the database, returns the new patient object
    @PostMapping("/addPatient")
    public Patient addPatient(@Valid @RequestBody AddPatientDTO addPatientDTO) {
        return patientService.addPatient(addPatientDTO);
    }

    //Update the patient details using patient ID, returns the updated patient object
    @PutMapping("/updatePatient/{id}")
    public Patient updatePatient(@PathVariable String id, @Valid @RequestBody AddPatientDTO addPatientDTO)
            throws PatientNotFoundException {
        return patientService.updatePatient(id, addPatientDTO);
    }

    //Find patient using patient ID, returns the patient object
    @GetMapping("/findPatient/{patientId}")
    public Patient findPatient(@PathVariable String patientId) throws PatientNotFoundException {
        return patientService.findPatientById(patientId);
    }
}
