// hospital.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Hospital, AddHospitalRequest } from '../models/hospital.requests';

@Injectable({
  providedIn: 'root'
})
export class HospitalService {
  private apiUrl = 'http://localhost:8080/api/hospitals'; // Update with your API URL

  constructor(private http: HttpClient) {}

  addHospital(addHospitalRequest: AddHospitalRequest): Observable<Hospital> {
    return this.http.post<Hospital>(`${this.apiUrl}/add`, addHospitalRequest);
  }

  updateHospital(id: string, addHospitalRequest: AddHospitalRequest): Observable<Hospital> {
    return this.http.put<Hospital>(`${this.apiUrl}/update/${id}`, addHospitalRequest);
  }

  getHospitalById(id: string): Observable<Hospital> {
    return this.http.get<Hospital>(`${this.apiUrl}/findById/${id}`);
  }
  getAllHospitals(): Observable<Hospital[]> {
    return this.http.get<Hospital[]>(`${this.apiUrl}/getAllHospitals`);
  }
  isHospitalVerified(id: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/isVerified/${id}`);
  }

  verifyHospital(id: string): Observable<Hospital> {
    return this.http.put<Hospital>(`${this.apiUrl}/verify/${id}`, {});
  }

  getNearbyHospitals(pincode: number): Observable<Hospital[]> {
    return this.http.get<Hospital[]>(`${this.apiUrl}/nearby/${pincode}`);
  }
}
