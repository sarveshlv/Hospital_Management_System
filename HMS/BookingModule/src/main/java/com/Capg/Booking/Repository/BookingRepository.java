package com.Capg.Booking.Repository;

import com.Capg.Booking.Model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findAllByPatientId(String patientId);

    List<Booking> findAllByHospitalId(String hospitalId);
}
