import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  AddUserRequest,
  UpdateUserRequest,
  UpdatePasswordRequest,
  UserDetails,
  LoginRequest,
} from '../models/user.requests';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private baseUserUrl = 'http://localhost:8000/api/users';
  private baseAuthUrl = 'http://localhost:8600/api/auth';

  constructor(private httpClient: HttpClient) {}

  login(loginRequest: LoginRequest): Observable<any>{
    return this.httpClient.post<any>(`${this.baseAuthUrl}/login`, loginRequest);
  }

  addUser(registerRequest: AddUserRequest): Observable<any> {
    return this.httpClient.post(`${this.baseUserUrl}/signup`, registerRequest);
  }

  updateUser(updateUserRequest: UpdateUserRequest): Observable<any> {
    return this.httpClient.put(`${this.baseUserUrl}/update`, updateUserRequest);
  }

  getUserByEmail(email: string): Observable<any> {
    return this.httpClient.get(`${this.baseUserUrl}/findByEmail/${email}`);
  }

  updatePassword(updatePasswordRequest: UpdatePasswordRequest): Observable<any> {
    return this.httpClient.put(`${this.baseUserUrl}/updatePassword`, updatePasswordRequest);
  }

  addReference(emailId: string, referenceId: string): Observable<any> {
    return this.httpClient.get(`${this.baseUserUrl}/addReference/${emailId}/${referenceId}`);
  }
}