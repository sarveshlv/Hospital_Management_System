package com.Capg.Hospital.Service;

import com.Capg.Hospital.Constants.HospitalType;
import com.Capg.Hospital.DTO.AddHospitalDTO;
import com.Capg.Hospital.DTO.UpdateHospitalDTO;
import com.Capg.Hospital.Exceptions.HospitalNotFoundException;
import com.Capg.Hospital.Exceptions.InvalidHospitalTypeException;
import com.Capg.Hospital.Model.Hospital;
import com.Capg.Hospital.Repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public Hospital registerHospital(AddHospitalDTO hospitalDTO) {

        Hospital obj = new Hospital();
        obj.setHospitalName(hospitalDTO.getHospitalName());
        obj.setHospitalAddress(hospitalDTO.getHospitalAddress());
        obj.setPincode(hospitalDTO.getPincode());
        obj.setContactNo(hospitalDTO.getContactNo());
        obj.setStatus("REQUESTED");

        String hospitalType = hospitalDTO.getHospitalType();
        HospitalType typeObj = toEnum(hospitalType);
        obj.setHospitalType(typeObj);

        return hospitalRepository.save(obj);
    }

    @Override
    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    @Override
    public Hospital updateHospital(String id, UpdateHospitalDTO updateHospitalDTO) throws HospitalNotFoundException {
        Hospital hospital = findHospitalById(id);
        hospital.setContactNo(updateHospitalDTO.getContactNo());
        hospital.setHospitalAddress(updateHospitalDTO.getHospitalAddress());
        hospital.setPincode(updateHospitalDTO.getPincode());
        return hospitalRepository.save(hospital);
    }

    @Override
    public Hospital verifyHospital(String hospitalId, String status) {

        Optional<Hospital> obj = hospitalRepository.findById(hospitalId);
        if (!obj.isPresent()) {
            throw new HospitalNotFoundException(hospitalId);
        }

        Hospital object = obj.get();

        if (status.equalsIgnoreCase("APPROVE")) {
            object.setStatus("APPROVED");
            return hospitalRepository.save(object);
        } else if (status.equalsIgnoreCase("REJECT")) {
            object.setStatus("DECLINED");
//          hospitalRepository.deleteById(object.getHospitalId());
        }
        return hospitalRepository.save(object);
    }

    @Override
    public List<Hospital> findNearbyHospitals(Long pincode) {
        List<Hospital> hospitalList = hospitalRepository.getHospitalByPincode(pincode - 1, pincode + 1);
        if (hospitalList.isEmpty()) {
            throw new HospitalNotFoundException("There are no hospitals near to you. Sorry!");
        }
        return hospitalList;
    }

    @Override
    public Hospital findHospitalById(String hospitalId) {
        Optional<Hospital> obj = hospitalRepository.findById(hospitalId);
        if (obj.isEmpty()) {
            throw new HospitalNotFoundException("Hospital not found with ID: " + hospitalId);
        }
        return obj.get();
    }

    @Override
    public Hospital deleteHospitalById(String hospitalId) {
        Hospital obj = findHospitalById(hospitalId);
        hospitalRepository.deleteById(hospitalId);
        return obj;
    }

    public HospitalType toEnum(String hospitalType) {
        HospitalType[] types = HospitalType.values();
        for (HospitalType itr : types) {
            String itrType = itr.toString();
            if (itrType.equalsIgnoreCase(hospitalType))
                return itr;
        }
        throw new InvalidHospitalTypeException("Invalid Hospital Type!");
    }
}
