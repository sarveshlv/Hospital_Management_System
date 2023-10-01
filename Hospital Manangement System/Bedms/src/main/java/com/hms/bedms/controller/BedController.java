package com.hms.bedms.controller;

import com.hms.bedms.dtos.AddBedRequest;
import com.hms.bedms.dtos.UpdateBedRequest;
import com.hms.bedms.entities.Bed;
import com.hms.bedms.exceptions.BedNotFoundException;
import com.hms.bedms.exceptions.BedStatusInvalidException;
import com.hms.bedms.exceptions.HospitalNotFoundException;
import com.hms.bedms.service.BedService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beds")
@Validated
public class BedController {

	@Autowired
	private BedService bedService;

	@PostMapping("/add")
	public ResponseEntity<Bed> addBed(@RequestHeader("Authorization") String authorizationHeader, @Valid @RequestBody AddBedRequest addBedRequest)
			throws HospitalNotFoundException {
		return ResponseEntity.ok().body(bedService.addBed(authorizationHeader, addBedRequest));
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Bed> updateBed(@PathVariable String id, @Valid @RequestBody UpdateBedRequest updateBedRequest)
			throws BedNotFoundException {
		return ResponseEntity.ok().body(bedService.updateBed(id, updateBedRequest));
	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<Bed> findBedById(@PathVariable String id) throws BedNotFoundException {
		return ResponseEntity.ok().body(bedService.findBedById(id));
	}

	@GetMapping("/findAll")
	public List<Bed> getAllBeds() {
		return bedService.getAllBeds();
	}

	@GetMapping("/findNearby/{pincode}")
	public ResponseEntity<List<Bed>> getNearbyBeds(@RequestHeader("Authorization") String authorizationHeader, 
			@NotNull(message = "Pincode is required") @Digits(integer = 6, fraction = 0, message = "Pincode must be a 6-digit number") @PathVariable Long pincode) {
		return ResponseEntity.ok().body(bedService.getNearbyBeds(authorizationHeader, pincode));
	}

	@GetMapping("/findByType/{bedType}")
	public ResponseEntity<List<Bed>> getBedsByType(
			@NotBlank(message = "Bed type is required") @Pattern(regexp = "USUAL_BED|ICU_BED|OXYGEN_BED|VENTILATOR_BED", message = "Invalid Bed type") @PathVariable String bedType) {
		return ResponseEntity.ok().body(bedService.getBedsByType(bedType));
	}

	@GetMapping("/findByHospital/{hospitalId}")
	public ResponseEntity<List<Bed>> getBedsByHospitalId(@PathVariable String hospitalId) {
		return ResponseEntity.ok().body(bedService.getBedsByHospitalId(hospitalId));
	}

	@PutMapping("/bookBed/{id}")
	public ResponseEntity<Bed> bookBed(@PathVariable String id) throws BedNotFoundException, BedStatusInvalidException  {
		return ResponseEntity.ok().body(bedService.bookBed(id));
	}

	@PutMapping("/unbookBed/{id}")
	public ResponseEntity<Bed> unbookBed(@PathVariable String id) throws BedNotFoundException, BedStatusInvalidException  {
		return ResponseEntity.ok().body(bedService.unbookBed(id));
	}

	@PutMapping("/completeBed/{id}")
	public ResponseEntity<Bed> completeBed(@PathVariable String id) throws BedNotFoundException, BedStatusInvalidException{
		return ResponseEntity.ok().body(bedService.completeBed(id));
	}
	@PutMapping("/makeBedAvailable/{id}")
	public ResponseEntity<Bed> makeBedAvailable(@PathVariable String id) throws BedNotFoundException, BedStatusInvalidException  {
		return ResponseEntity.ok().body(bedService.makeBedAvailable(id));
	}
}