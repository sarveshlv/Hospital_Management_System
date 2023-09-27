import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AddBedRequest, UpdateBedRequest, Bed } from '../models/bed.requests';

@Injectable({
  providedIn: 'root',
})
export class BedService {
  private baseUrl = 'http://localhost:8200/api/beds';

  constructor(private httpClient: HttpClient) {}

  addBed(authorizationHeader: string, addBedRequest: AddBedRequest): Observable<Bed> {
    const headers = new HttpHeaders({ 'Authorization': authorizationHeader });
    return this.httpClient.post<Bed>(`${this.baseUrl}/add`, addBedRequest, { headers });
  }

  updateBed(id: string, updateBedRequest: UpdateBedRequest): Observable<Bed> {
    return this.httpClient.put<Bed>(`${this.baseUrl}/update/${id}`, updateBedRequest);
  }

  findBedById(id: string): Observable<Bed> {
    return this.httpClient.get<Bed>(`${this.baseUrl}/findById/${id}`);
  }

  getAllBeds(): Observable<Bed[]> {
    return this.httpClient.get<Bed[]>(`${this.baseUrl}/findAll`);
  }

  getNearbyBeds(authorizationHeader: string, pincode: number): Observable<Bed[]> {
    const headers = new HttpHeaders({ 'Authorization': authorizationHeader });
    return this.httpClient.get<Bed[]>(`${this.baseUrl}/findNearby/${pincode}`, { headers });
  }

  getBedsByType(bedType: string): Observable<Bed[]> {
    return this.httpClient.get<Bed[]>(`${this.baseUrl}/findByType/${bedType}`);
  }

  getBedsByHospitalId(hospitalId: string): Observable<Bed[]> {
    return this.httpClient.get<Bed[]>(`${this.baseUrl}/findByHospital/${hospitalId}`);
  }

  bookBed(id: string): Observable<Bed> {
    return this.httpClient.put<Bed>(`${this.baseUrl}/bookBed/${id}`, {});
  }

  unbookBed(id: string): Observable<Bed> {
    return this.httpClient.put<Bed>(`${this.baseUrl}/unbookBed/${id}`, {});
  }

  makeBedAvailable(id: string): Observable<Bed> {
    return this.httpClient.put<Bed>(`${this.baseUrl}/makeBedAvailable/${id}`, {});
  }
}