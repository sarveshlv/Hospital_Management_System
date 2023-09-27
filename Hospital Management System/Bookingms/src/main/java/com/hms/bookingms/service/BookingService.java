package com.hms.bookingms.service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hms.bookingms.clients.IBedServiceClient;
import com.hms.bookingms.clients.IHospitalServiceClient;
import com.hms.bookingms.clients.IPatientServiceClient;
import com.hms.bookingms.dto.AddBookingRequest;
import com.hms.bookingms.dto.Bed;
import com.hms.bookingms.entities.Booking;
import com.hms.bookingms.exceptions.BookingNotFoundException;
import com.hms.bookingms.exceptions.HospitalNotFoundException;
import com.hms.bookingms.exceptions.InvalidBookingRequest;
import com.hms.bookingms.exceptions.InvalidDatesException;
import com.hms.bookingms.exceptions.PatientNotFoundException;
import com.hms.bookingms.repository.BookingRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookingService implements IBookingService {

	@Autowired
	private IBedServiceClient bedServiceClient;

	@Autowired
	private IPatientServiceClient patientServiceClient;

	@Autowired
	private IHospitalServiceClient hospitalServiceClient;

	@Autowired
	private BookingRepository bookingRepository;

	@Override
	public Booking addBooking(String authorizationHeader, AddBookingRequest addBookingRequest)
			throws PatientNotFoundException, HospitalNotFoundException, InvalidDatesException {
		String patientId = addBookingRequest.getPatientId();
		String hospitalId = addBookingRequest.getHospitalId();

		Date bookingDate = new Date();
		Date occupyDate = addBookingRequest.getOccupyDate();
		Date releaseDate = addBookingRequest.getReleaseDate();

		ResponseEntity<?> reponse = patientServiceClient.isPatientFound(authorizationHeader, patientId);
		log.info(reponse.toString());
		if (patientServiceClient.isPatientFound(authorizationHeader, patientId).getStatusCode() != HttpStatus.OK) {
			throw new PatientNotFoundException(patientId);
		} else if (hospitalServiceClient.isHospitalFound(authorizationHeader, hospitalId).getStatusCode() != HttpStatus.OK) {
			throw new HospitalNotFoundException(hospitalId);
		} else if (occupyDate.before(bookingDate) || releaseDate.before(occupyDate)) {
			throw new InvalidDatesException(bookingDate, occupyDate, releaseDate);
		}

		Booking booking = new Booking();

		booking.setPatientId(patientId);
		booking.setHospitalId(hospitalId);
		booking.setBedType(Booking.BedType.valueOf(addBookingRequest.getBedType()));
		booking.setBookingStatus(Booking.BookingStatus.REQUESTED);
		booking.setBookingDate(new Date());
		booking.setOccupyDate(occupyDate);
		booking.setReleaseDate(releaseDate);

		return bookingRepository.save(booking);
	}

	@Override
	public Booking findBookingById(String bookingId) throws BookingNotFoundException {
		return bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(bookingId));
	}

	@Override
	public List<Booking> getBookingByPatientId(String patientId) throws PatientNotFoundException {
		List<Booking> bookings = bookingRepository.findAllByPatientId(patientId);
		if (bookings.isEmpty()) {
			throw new PatientNotFoundException(patientId);
		}
		return bookings;
	}

	@Override
	public Booking approveBooking(String authorizationHeader, String bookingId)
			throws BookingNotFoundException, InvalidBookingRequest, RuntimeException {
		Booking booking = findBookingById(bookingId);
		if (booking.getBookingStatus().equals(Booking.BookingStatus.REQUESTED)) {

			List<Bed> beds = bedServiceClient.findBedsByType(authorizationHeader, booking.getBedType());
			if (beds.isEmpty()) {
				throw new RuntimeException("No beds found for bed type: " + booking.getBedType());
			}
			int randomIndex = new Random().nextInt(beds.size());
			Bed randomBed = beds.get(randomIndex);
			String bedId = randomBed.getId();
			;
			bedServiceClient.bookBed(authorizationHeader, randomBed.getId());

			booking.setBookingStatus(Booking.BookingStatus.APPROVED);
			booking.setBedId(bedId);
			return bookingRepository.save(booking);
		}
		throw new InvalidBookingRequest(booking.getBookingStatus().toString());

	}

	@Override
	public Booking rejectBooking(String bookingId) throws BookingNotFoundException, InvalidBookingRequest {
		Booking booking = findBookingById(bookingId);
		if (booking.getBookingStatus().equals(Booking.BookingStatus.REQUESTED)) {
			booking.setBookingStatus(Booking.BookingStatus.DECLINED);
			return bookingRepository.save(booking);
		}
		throw new InvalidBookingRequest(booking.getBookingStatus().toString());
	}

	@Override
	public Booking cancelBooking(String authorizationHeader, String bookingId) throws BookingNotFoundException, InvalidBookingRequest {
		Booking booking = findBookingById(bookingId);
		if (booking.getBookingStatus().equals(Booking.BookingStatus.APPROVED)) {
			booking.setBookingStatus(Booking.BookingStatus.CANCELLED);
			bedServiceClient.unbookBed(authorizationHeader, booking.getBedId());
			return bookingRepository.save(booking);
		}
		throw new InvalidBookingRequest(booking.getBookingStatus().toString());
	}

	@Override
	public Booking completeBooking(String authorizationHeader, String bookingId) {
		Booking booking = findBookingById(bookingId);
		if (booking.getBookingStatus().equals(Booking.BookingStatus.APPROVED)) {
			booking.setBookingStatus(Booking.BookingStatus.COMPLETED);
			bedServiceClient.unbookBed(authorizationHeader, booking.getBedId());
			return bookingRepository.save(booking);
		}
		throw new InvalidBookingRequest(booking.getBookingStatus().toString());
	}

	@Override
	public List<Booking> getBookingByHospitalId(String hospitalId) throws HospitalNotFoundException {
		List<Booking> bookings = bookingRepository.findAllByHospitalId(hospitalId);
		if (bookings.isEmpty()) {
			throw new HospitalNotFoundException(hospitalId);
		}
		return bookings;
	}
}