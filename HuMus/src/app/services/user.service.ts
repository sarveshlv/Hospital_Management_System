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
  private baseUserUrl = 'http://localhost:8080/api/users';
  private baseAuthUrl = 'http://localhost:8080/api/auth';

  constructor(private httpClient: HttpClient) {}

  login(loginRequest: LoginRequest): Observable<any>{
    return this.httpClient.post<any>(`${this.baseAuthUrl}/login`, loginRequest);
  }

  logout(email: string): Observable<any>{
    return this.httpClient.get<UserDetails>(`${this.baseAuthUrl}/logout/${email}`);
  }
  
  addUser(registerRequest: AddUserRequest): Observable<UserDetails> {
    return this.httpClient.post<UserDetails>(`${this.baseUserUrl}/signup`, registerRequest);
  }

  updateUser(updateUserRequest: UpdateUserRequest): Observable<UserDetails> {
    return this.httpClient.put<UserDetails>(`${this.baseUserUrl}/update`, updateUserRequest);
  }

  getUserByEmail(email: string): Observable<UserDetails> {
    return this.httpClient.get<UserDetails>(`${this.baseUserUrl}/findByEmail/${email}`);
  }

  updatePassword(updatePasswordRequest: UpdatePasswordRequest): Observable<UserDetails> {
    return this.httpClient.put<UserDetails>(`${this.baseUserUrl}/updatePassword`, updatePasswordRequest);
  }

  addReference(emailId: string, referenceId: string): Observable<UserDetails> {
    return this.httpClient.get<UserDetails>(`${this.baseUserUrl}/addReference/${emailId}/${referenceId}`);
  }
}