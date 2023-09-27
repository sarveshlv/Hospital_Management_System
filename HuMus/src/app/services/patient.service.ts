import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AddPatientRequest, Patient } from '../models/patient.requests';

@Injectable({
  providedIn: 'root',
})
export class PatientService {
  private baseUrl = 'http://localhost:8300/api/patients';

  constructor(private httpClient: HttpClient) {}

  addPatient(addPatientRequest: AddPatientRequest): Observable<Patient> {
    return this.httpClient.post<Patient>(`${this.baseUrl}/add`, addPatientRequest);
  }

  updatePatient(id: string, addPatientRequest: AddPatientRequest): Observable<Patient> {
    return this.httpClient.put<Patient>(`${this.baseUrl}/update/${id}`, addPatientRequest);
  }

  findPatientById(id: string): Observable<Patient> {
    return this.httpClient.get<Patient>(`${this.baseUrl}/findById/${id}`);
  }
}
