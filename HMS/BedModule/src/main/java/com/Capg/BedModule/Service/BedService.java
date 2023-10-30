package com.Capg.BedModule.Service;

import com.Capg.BedModule.DTO.AddBedDTO;
import com.Capg.BedModule.Model.Bed;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BedService {


    Bed registerBed(String authorizationHeader, AddBedDTO addBedObj);

    Bed updateBedDetails(String bedId, Double cost);

    List<Bed> getAllBeds();

    List<Bed> getBedsByHospitalId(String hospitalId);

    List<Bed> getNearByBeds(String authorizationHeader, Long pincode);

    List<Bed> getBedsByType(String bedType);

    Bed bookBed(String bedId);

    Bed makeBedAvailable(String bedId);

    Bed deleteBed(String bedId);
}
