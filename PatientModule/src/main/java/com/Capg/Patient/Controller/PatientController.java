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

    @PostMapping("/addPatient")
    public Patient addPatient(@Valid @RequestBody AddPatientDTO addPatientDTO) {
        return patientService.addPatient(addPatientDTO);
    }

    @PutMapping("/updatePatient/{id}")
    public Patient updatePatient(@PathVariable String id, @Valid @RequestBody AddPatientDTO addPatientDTO)
            throws PatientNotFoundException {
        return patientService.updatePatient(id, addPatientDTO);
    }

    @GetMapping("/findPatient/{patientId}")
    public Patient findPatient(@PathVariable String patientId) throws PatientNotFoundException {
        return patientService.findPatientById(patientId);
    }
}
