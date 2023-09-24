package com.hms.bookingms.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

import com.hms.bookingms.entities.Booking;

public interface BookingRepository extends MongoRepository<Booking, String> {
	List<Booking> findAllByPatientId(String patientId);

	List<Booking> findAllByHospitalId(String hospitalId);
}
