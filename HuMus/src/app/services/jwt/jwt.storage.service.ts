import { Injectable, signal } from '@angular/core';
import { UserDetails } from 'src/app/models/user.requests';

@Injectable({
  providedIn: 'root',
})
export class JwtStorageService {
  private jwtToken: string | null = null;
  private userDetails: UserDetails | null = null;

  authenticated = signal(false);

  setToken(token: string) {
    this.jwtToken = token;
  }

  getToken(): string | null {
    return this.jwtToken;
  }

  clearToken() {
    this.jwtToken = null;
    this.userDetails = null;
  }

  setUserDetails(userDetails: UserDetails) {
    this.userDetails = userDetails;
  }

  getUserDetails(): UserDetails | null {
    return this.userDetails;
  }

  isAuthenticated(): boolean {
    return !!this.jwtToken;
  }
}
