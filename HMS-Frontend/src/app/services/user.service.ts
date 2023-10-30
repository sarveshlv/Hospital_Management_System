import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginRequest, RegisterUserRequest, UpdateUserRequest, UserDetails } from '../models/user.requests';
import { User } from '../models/User.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl = 'http://localhost:9090/User';
  private baseauthUrl = 'http://localhost:9090/Authenticate'

  constructor(private httpClient: HttpClient) { }

  signin(loginRequest: LoginRequest): Observable<any> {
    return this.httpClient.post<any>(`${this.baseauthUrl}/signin`, loginRequest);
  }

  logout(email: string): Observable<any> {
    return this.httpClient.get<UserDetails>(`${this.baseauthUrl}/logout/${email}`);
  }

  registerUser(registerRequest: RegisterUserRequest): Observable<User> {
    return this.httpClient.post<User>(`${this.baseUrl}/signup`, registerRequest);
  }

  updateUser(updateRequest: UpdateUserRequest): Observable<User> {
    return this.httpClient.put<User>(`${this.baseUrl}/updateUser`, updateRequest);
  }

  getUserByEmail(emailId: string): Observable<User> {
    return this.httpClient.get<User>(`${this.baseUrl}/findByEmailId/${emailId}`);
  }

  addReference(emailId: string, referenceId: string): Observable<User> {
    return this.httpClient.get<User>(`${this.baseUrl}/addReference/${emailId}/${referenceId}`);
  }
}
