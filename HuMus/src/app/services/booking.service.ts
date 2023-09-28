import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AddBookingRequest, Booking } from '../models/booking.requests';

@Injectable({
  providedIn: 'root',
})
export class BookingService {
  private baseUrl = 'http://localhost:8080/api/bookings';

  constructor(private httpClient: HttpClient) {}

  addBooking(authorizationHeader: string, addBookingRequest: AddBookingRequest): Observable<Booking> {
    const headers = new HttpHeaders({ 'Authorization': authorizationHeader });
    return this.httpClient.post<Booking>(`${this.baseUrl}/add`, addBookingRequest, { headers });
  }

  findBookingById(bookingId: string): Observable<Booking> {
    return this.httpClient.get<Booking>(`${this.baseUrl}/findById/${bookingId}`);
  }

  getBookingsByPatientId(patientId: string): Observable<Booking[]> {
    return this.httpClient.get<Booking[]>(`${this.baseUrl}/findByPatientId/${patientId}`);
  }

  getBookingByHospitalId(hospitalId: string): Observable<Booking[]> {
    return this.httpClient.get<Booking[]>(`${this.baseUrl}/findByHospitalId/${hospitalId}`);
  }

  approveBooking(authorizationHeader: string, bookingId: string): Observable<Booking> {
    const headers = new HttpHeaders({ 'Authorization': authorizationHeader });
    return this.httpClient.get<Booking>(`${this.baseUrl}/approve/${bookingId}`, { headers });
  }

  rejectBooking(bookingId: string): Observable<Booking> {
    return this.httpClient.get<Booking>(`${this.baseUrl}/reject/${bookingId}`);
  }

  cancelBooking(authorizationHeader: string, bookingId: string): Observable<Booking> {
    const headers = new HttpHeaders({ 'Authorization': authorizationHeader });
    return this.httpClient.get<Booking>(`${this.baseUrl}/cancel/${bookingId}`, { headers });
  }

  completeBooking(authorizationHeader: string, bookingId: string): Observable<Booking> {
    const headers = new HttpHeaders({ 'Authorization': authorizationHeader });
    return this.httpClient.get<Booking>(`${this.baseUrl}/complete/${bookingId}`, { headers });
  }
}