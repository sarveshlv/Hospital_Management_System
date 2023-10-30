import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AddBedRequest, Bed } from '../models/bed.requests';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BedService {

  private bedUrl = 'http://localhost:9090/Bed';

  constructor(private httpClient: HttpClient) { }

  registerBed(authorizationHeader: string, addBedObj: AddBedRequest): Observable<Bed> {
    const headers = new HttpHeaders({ 'Authorization': authorizationHeader });
    return this.httpClient.post<Bed>(`${this.bedUrl}/register`, addBedObj, { headers });
  }

  updateBedDetails(bedId: string, cost: number): Observable<Bed> {
    return this.httpClient.put<Bed>(`${this.bedUrl}/updateBed/${bedId}/${cost}`, {});
  }

  // Get all beds
  getAllBeds(): Observable<Bed[]> {
    return this.httpClient.get<Bed[]>(`${this.bedUrl}/getAllBeds`);
  }

  // Get beds by hospital ID
  getBedsByHospitalId(hospitalId: string): Observable<Bed[]> {
    return this.httpClient.get<Bed[]>(`${this.bedUrl}/getByHospital/${hospitalId}`);
  }

  // Get nearby beds by pincode
  getNearByBeds(authorizationHeader: string, pincode: number): Observable<Bed[]> {
    const headers = new HttpHeaders({ 'Authorization': authorizationHeader });
    return this.httpClient.get<Bed[]>(`${this.bedUrl}/getNearByBeds/${pincode}`, { headers });
  }

  // Get beds by type
  getBedsByType(bedType: string): Observable<Bed[]> {
    return this.httpClient.get<Bed[]>(`${this.bedUrl}/getByType/${bedType}`);
  }

  // Book a bed
  bookBed(bedId: string): Observable<Bed> {
    return this.httpClient.put<Bed>(`${this.bedUrl}/bookBed/${bedId}`, null);
  }

  // Make a bed available
  makeBedAvailable(bedId: string): Observable<Bed> {
    return this.httpClient.post<Bed>(`${this.bedUrl}/makeBedAvailable/${bedId}`, null);
  }

  deleteBed(bedId: string): Observable<Bed> {
    return this.httpClient.delete<Bed>(`${this.bedUrl}/deleteBed/${bedId}`);
  }
}
