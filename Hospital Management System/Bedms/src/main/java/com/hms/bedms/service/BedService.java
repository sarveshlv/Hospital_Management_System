package com.hms.bedms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hms.bedms.clients.IHospitalServiceClient;
import com.hms.bedms.dtos.AddBedRequest;
import com.hms.bedms.dtos.Hospital;
import com.hms.bedms.dtos.UpdateBedRequest;
import com.hms.bedms.entities.Bed;
import com.hms.bedms.entities.Bed.BedStatus;
import com.hms.bedms.entities.Bed.BedType;
import com.hms.bedms.exceptions.BedNotFoundException;
import com.hms.bedms.exceptions.BedStatusInvalidException;
import com.hms.bedms.exceptions.HospitalNotFoundException;
import com.hms.bedms.repository.IBedRepository;

@Service
public class BedService implements IBedService {

	@Autowired
	private IBedRepository bedRepository;

	@Autowired
	private IHospitalServiceClient hospitalServiceClient;

	@Override
	public Bed addBed(String authorizationHeader, AddBedRequest addBedRequest) throws HospitalNotFoundException {
		String hospitalId = addBedRequest.getHospitalId();

		if (!(hospitalServiceClient.isHospitalFound(authorizationHeader, hospitalId)
				.getStatusCode() == HttpStatus.OK)) {
			throw new HospitalNotFoundException(hospitalId);
		}

		Bed bed = new Bed();
		bed.setHospitalId(hospitalId);
		bed.setBedType(BedType.valueOf(addBedRequest.getBedType()));
		bed.setBedStatus(BedStatus.AVAILABLE);
		bed.setCostPerDay(addBedRequest.getCostPerDay());

		return bedRepository.save(bed);
	}

	@Override
	public Bed updateBed(String id, UpdateBedRequest updateBedRequest) throws BedNotFoundException {
		Bed bed = findBedById(id);

		bed.setBedType(BedType.valueOf(updateBedRequest.getBedType()));
		bed.setCostPerDay(updateBedRequest.getCostPerDay());

		return bedRepository.save(bed);
	}

	@Override
	public Bed findBedById(String id) throws BedNotFoundException {
		return bedRepository.findById(id).orElseThrow(() -> new BedNotFoundException(id));
	}

	@Override
	public List<Bed> getAllBeds() {
		return bedRepository.findAll();
	}

	@Override
	public List<Bed> getNearbyBeds(String authorizationHeader, Long pincode) {
		List<Bed> beds = new ArrayList<>();
		List<Hospital> nearbyHospitals = hospitalServiceClient.getNearbyHospitals(authorizationHeader, pincode);
		for (Hospital hospital : nearbyHospitals) {
			String hospitalId = hospital.getId();
			beds.addAll(getBedsByHospitalId(hospitalId));
		}
		return beds;
	}

	@Override
	public List<Bed> getBedsByType(String bedType) {
		return bedRepository.findByBedType(bedType);
	}

	@Override
	public List<Bed> getBedsByHospitalId(String hospitalId) {
		return bedRepository.findByHospitalId(hospitalId);
	}

	@Override
	public Bed bookBed(String id) throws BedNotFoundException, BedStatusInvalidException {
		Bed bed = findBedById(id);
		if (bed.getBedStatus() != BedStatus.AVAILABLE) {
			throw new BedStatusInvalidException(bed.getBedStatus().toString());
		}
		bed.setBedStatus(BedStatus.BOOKED);

		return bedRepository.save(bed);
	}

	@Override
	public Bed unbookBed(String id) throws BedNotFoundException, BedStatusInvalidException {
		Bed bed = findBedById(id);
		if (bed.getBedStatus() != BedStatus.BOOKED) {
			throw new BedStatusInvalidException(bed.getBedStatus().toString());
		}
		bed.setBedStatus(BedStatus.CANCELLED);
		return bedRepository.save(bed);
	}

	@Override
	public Bed makeBedAvailable(String id) throws BedNotFoundException, BedStatusInvalidException {
		Bed bed = findBedById(id);
		if (bed.getBedStatus() != BedStatus.CANCELLED) {
			throw new BedStatusInvalidException(bed.getBedStatus().toString());
		}
		bed.setBedStatus(BedStatus.AVAILABLE);
		return bedRepository.save(bed);
	}

	public Bed completeBed(String id) throws BedNotFoundException, BedStatusInvalidException {
		Bed bed = findBedById(id);
		if (bed.getBedStatus() != BedStatus.BOOKED) {
			throw new BedStatusInvalidException(bed.getBedStatus().toString());
		}
		bed.setBedStatus(BedStatus.COMPLETED);
		return bedRepository.save(bed);
	}
}
