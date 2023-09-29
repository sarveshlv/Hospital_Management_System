package com.hms.bedms.service;

import java.util.List;

import com.hms.bedms.dtos.AddBedRequest;
import com.hms.bedms.dtos.UpdateBedRequest;
import com.hms.bedms.entities.Bed;
import com.hms.bedms.exceptions.BedNotFoundException;
import com.hms.bedms.exceptions.BedStatusInvalidException;
import com.hms.bedms.exceptions.HospitalNotFoundException;

import jakarta.validation.Valid;

public interface IBedService {
	Bed addBed(String authorizationHeader, AddBedRequest addBedRequest) throws HospitalNotFoundException;

	Bed updateBed(String id, UpdateBedRequest updateBedRequest) throws BedNotFoundException;

	Bed findBedById(String id) throws BedNotFoundException;

	Bed bookBed(String id) throws BedNotFoundException, BedStatusInvalidException ;

	Bed unbookBed(String id) throws BedNotFoundException, BedStatusInvalidException ;

	Bed makeBedAvailable(String id) throws BedNotFoundException, BedStatusInvalidException ;

	List<Bed> getAllBeds();

	List<Bed> getNearbyBeds(String authorizationHeader,@Valid Long pincode);

	List<Bed> getBedsByType(@Valid String bedType);

	List<Bed> getBedsByHospitalId(String hospitalId) throws HospitalNotFoundException;
}
