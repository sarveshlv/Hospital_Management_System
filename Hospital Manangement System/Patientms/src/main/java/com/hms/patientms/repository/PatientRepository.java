package com.hms.patientms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.hms.patientms.entities.Patient;

public interface PatientRepository extends MongoRepository<Patient, String>{

}
