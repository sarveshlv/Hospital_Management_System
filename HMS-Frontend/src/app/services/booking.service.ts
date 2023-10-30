import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AddBookingRequest } from '../models/booking.requests';
import { Booking } from '../models/booking.requests';

@Injectable({
  providedIn: 'root'
})
export class BookingService {

  private bookingUrl = 'http://localhost:9090/Booking';

  constructor(private httpClient: HttpClient) { }

  addBooking(authorizationHeader: string, addBookingDTO: AddBookingRequest): Observable<Booking> {
    const headers = new HttpHeaders({ 'Authorization': authorizationHeader });
    return this.httpClient.post<Booking>(`${this.bookingUrl}/addBooking`, addBookingDTO, { headers });
  }

  // Method to get a booking by ID
  getBookingById(bookingId: string): Observable<Booking> {
    return this.httpClient.get<Booking>(`${this.bookingUrl}/findBooking/${bookingId}`);
  }

  // Method to get bookings by Booking ID
  getBookingsByPatientId(patientId: string): Observable<Booking[]> {
    return this.httpClient.get<Booking[]>(`${this.bookingUrl}/findByPatientId/${patientId}`);
  }

  // Method to get bookings by hospital ID
  getBookingByHospitalId(hospitalId: string): Observable<Booking[]> {
    return this.httpClient.get<Booking[]>(`${this.bookingUrl}/findByHospitalId/${hospitalId}`);
  }

  // Method to cancel a booking
  cancelBooking(authorizationHeader: string, bookingId: string): Observable<Booking> {
    const headers = new HttpHeaders({ 'Authorization': authorizationHeader, });
    return this.httpClient.put<Booking>(`${this.bookingUrl}/cancelBooking/${bookingId}`, { headers });
  }

  approveBooking(bookingId: string): Observable<Booking> {
    return this.httpClient.put<Booking>(`${this.bookingUrl}/approveBooking/${bookingId}`, {});
  }

  declineBooking(authorizationHeader: string, bookingId: string): Observable<Booking> {
    const headers = new HttpHeaders({ 'Authorization' : authorizationHeader });
    return this.httpClient.put<Booking>(`${this.bookingUrl}/declineBooking/${bookingId}`, { headers })
  }
}
