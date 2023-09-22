package com.Capg.BedModule.Service;

import com.Capg.BedModule.DTO.AddBedDTO;
import com.Capg.BedModule.Model.Bed;
import org.springframework.stereotype.Service;

import java.util.List;

public interface BedService {
    Bed registerBed(AddBedDTO addBedObj);

    List<Bed> getAllBeds();

    List<Bed> getBedsByHospitalId(String hospitalId);

    List<Bed> getNearByBeds(Long pincode);

    List<Bed> getBedsByType(String bedType);

    Bed bookBed(String bedId);

    Bed makeBedAvailable(String bedId);
}
