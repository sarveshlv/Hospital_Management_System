package com.Capg.BedModule.Service;

import com.Capg.BedModule.Constants.BedStatus;
import com.Capg.BedModule.Constants.BedType;
import com.Capg.BedModule.DTO.AddBedDTO;
import com.Capg.BedModule.DTO.Hospital;
import com.Capg.BedModule.Exceptions.BedNotFoundException;
import com.Capg.BedModule.Exceptions.DeleteBedException;
import com.Capg.BedModule.Exceptions.HospitalNotFoundException;
import com.Capg.BedModule.FeignClients.HospitalServiceClient;
import com.Capg.BedModule.Model.Bed;
import com.Capg.BedModule.Repository.BedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BedServiceImpl implements BedService {

    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private HospitalServiceClient hospitalServiceClient;

    @Override
    public Bed registerBed(String authorizationHeader, AddBedDTO addBedObj) {
        String hospitalId = addBedObj.getHospitalId();

        //Check if the hospital is present in database by calling hospitl service using feign client service
        if (!(hospitalServiceClient.isHospitalFound(authorizationHeader, hospitalId).getStatusCode() == HttpStatus.OK)) {
            throw new HospitalNotFoundException("Hospital is not found with ID: " + hospitalId);
        }

        Bed obj = new Bed();
        obj.setHospitalId(hospitalId);
        obj.setBedType(BedType.valueOf(addBedObj.getBedType()));
        obj.setBedStatus(BedStatus.AVAILABLE);
        obj.setCostPerDay(addBedObj.getCostPerDay());

        return bedRepository.save(obj);
    }

    @Override
    public Bed updateBedDetails(String bedId, Double cost) {
        Optional<Bed> obj = bedRepository.findById(bedId);
        Bed bed = obj.get();
        bed.setCostPerDay(cost);
        return bedRepository.save(bed);
    }

    @Override
    public List<Bed> getAllBeds() {
        return bedRepository.findAll();
    }

    @Override
    public List<Bed> getBedsByHospitalId(String hospitalId) {
        return bedRepository.findByHospitalId(hospitalId);
    }

    @Override
    public List<Bed> getNearByBeds(String authorizationHeader, Long pincode) {
        List<Bed> beds = new ArrayList<>();

        //Check for all the nearest hospitals and then add only those hospitals to the filtered list
        List<Hospital> nearbyHospitals = hospitalServiceClient.getNearbyHospitals(authorizationHeader, pincode);
        for (Hospital obj : nearbyHospitals) {
            String hospitalId = obj.getHospitalId();
            beds.addAll(getBedsByHospitalId(hospitalId));
        }
        return beds;
    }

    @Override
    public List<Bed> getBedsByType(String bedType) {
        return bedRepository.findByBedType(bedType);
    }

    @Override
    public Bed bookBed(String bedId) {
        Optional<Bed> bed = bedRepository.findById(bedId);
        if (bed.isEmpty()) {
            throw new BedNotFoundException("Bed not found with ID: " + bedId);
        }
        Bed obj = bed.get();
        obj.setBedStatus(BedStatus.BOOKED);
        return bedRepository.save(obj);
    }

    @Override
    public Bed makeBedAvailable(String bedId) {
        Optional<Bed> bed = bedRepository.findById(bedId);
        if (bed.isEmpty()) {
            throw new BedNotFoundException("Bed not found with ID: " + bedId);
        }
        Bed obj = bed.get();
        obj.setBedStatus(BedStatus.AVAILABLE);
        return bedRepository.save(obj);
    }

    @Override
    public Bed deleteBed(String bedId) {
        Optional<Bed> obj = bedRepository.findById(bedId);
        Bed bed = obj.get();
        if (bed.getBedStatus() == BedStatus.BOOKED) {
            throw new DeleteBedException("Cannot delete bed as it is currently booked by a patient!");
        }
        bedRepository.deleteById(bedId);
        return bed;
    }
}
