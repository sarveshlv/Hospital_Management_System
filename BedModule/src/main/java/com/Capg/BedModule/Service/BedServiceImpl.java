package com.Capg.BedModule.Service;

import com.Capg.BedModule.Constants.BedStatus;
import com.Capg.BedModule.Constants.BedType;
import com.Capg.BedModule.DTO.AddBedDTO;
import com.Capg.BedModule.DTO.Hospital;
import com.Capg.BedModule.Exceptions.BedNotFoundException;
import com.Capg.BedModule.Model.Bed;
import com.Capg.BedModule.Repository.BedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BedServiceImpl implements BedService{

    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Bed registerBed(AddBedDTO addBedObj) {
        String hospitalId = addBedObj.getHospitalId();
        String HOSPITAL_URL = "http://localhost:9090/Hospital/getHospital/";

        restTemplate.getForObject(HOSPITAL_URL+hospitalId, Hospital.class);

        Bed obj = new Bed();
        obj.setBedId(hospitalId + "_" + addBedObj.getCostPerDay().intValue());
        obj.setHospitalId(hospitalId);
        obj.setBedType(BedType.valueOf(addBedObj.getBedType()));
        obj.setBedStatus(BedStatus.AVAILABLE);
        obj.setCostPerDay(addBedObj.getCostPerDay());

        return bedRepository.save(obj);
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
    public List<Bed> getNearByBeds(Long pincode) {
        List<Bed> beds = new ArrayList<>();
        String HOSPITAL_URL = "http://localhost:9090/Hospital/findNearbyHospitals/"+pincode;

        List<Hospital> response = Collections.singletonList(restTemplate.getForObject(HOSPITAL_URL, Hospital.class));
//        List<Hospital> response = restTemplate.getForObject(HOSPITAL_URL, List<Hospital>.class);
        for(Hospital obj: response) {
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
        if(bed.isEmpty()) {
            throw new BedNotFoundException("Bed not found with ID: "+bedId);
        }
        Bed obj = bed.get();
        obj.setBedStatus(BedStatus.BOOKED);
        return bedRepository.save(obj);
    }

    @Override
    public Bed makeBedAvailable(String bedId) {
        Optional<Bed> bed = bedRepository.findById(bedId);
        if(bed.isEmpty()) {
            throw new BedNotFoundException("Bed not found with ID: "+bedId);
        }
        Bed obj = bed.get();
        obj.setBedStatus(BedStatus.AVAILABLE);
        return bedRepository.save(obj);
    }
}
