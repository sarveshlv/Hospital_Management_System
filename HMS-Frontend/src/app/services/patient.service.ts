import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AddPatient, Patient } from '../models/patient.requests';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PatientService {

  constructor(private httpClient: HttpClient) { }

  private patientUrl = 'http://localhost:9090/Patient';

  addPatient(addPatient: AddPatient): Observable<Patient> {
    return this.httpClient.post<Patient>(`${this.patientUrl}/addPatient`, addPatient);
  }

  updatePatient(id: string, addPatient: AddPatient): Observable<Patient> {
    return this.httpClient.put<Patient>(`${this.patientUrl}/updatePatient/${id}`, addPatient);
  }

  findPatient(patientId: string): Observable<Patient> {
    return this.httpClient.get<Patient>(`${this.patientUrl}/findPatient/${patientId}`);
  }
}
