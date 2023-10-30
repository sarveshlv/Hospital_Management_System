import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Hospital } from '../models/hospital.requests';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HospitalService {

  private hospitalUrl = 'http://localhost:9090/Hospital';

  constructor(private httpClient: HttpClient) { }

  registerHospital(hospitalData: any): Observable<Hospital> {
    return this.httpClient.post<Hospital>(`${this.hospitalUrl}/register`, hospitalData);
  }

  getAllHospitals(): Observable<Hospital[]> {
    return this.httpClient.get<Hospital[]>(`${this.hospitalUrl}/getHospitals`);
  }

  updateHospital(id: string, hospitalData: any): Observable<Hospital> {
    return this.httpClient.put<Hospital>(`${this.hospitalUrl}/updateHospital/${id}`, hospitalData);
  }

  approveDeclineRegistration(hospitalId: string, status: string): Observable<Hospital> {
    return this.httpClient.put<Hospital>(`${this.hospitalUrl}/verifyRegistration/${hospitalId}?status=${status}`, {});
  }

  findNearbyHospitals(pincode: number): Observable<Hospital[]> {
    return this.httpClient.get<Hospital[]>(`${this.hospitalUrl}/findNearbyHospitals/${pincode}`);
  }

  getHospitalById(hospitalId: string): Observable<Hospital> {
    return this.httpClient.get<Hospital>(`${this.hospitalUrl}/getHospital/${hospitalId}`);
  }

  deleteHospitalById(hospitalId: string): Observable<Hospital> {
    return this.httpClient.delete<Hospital>(`${this.hospitalUrl}/deleteHospital/${hospitalId}`);
  }
}
