import { Injectable, signal } from '@angular/core';
import { UserDetails } from 'src/app/models/user.requests';

@Injectable({
  providedIn: 'root',
})
export class JwtService {
  private jwtToken: string | null = null;
  private user: UserDetails | null = null;

  authenticated = signal(false);

  setToken(token: string) {
    this.jwtToken = token;
  }

  getToken(): string | null {
    return this.jwtToken;
  }

  clearToken() {
    this.jwtToken = null;
    this.user = null;
  }

  setUserDetails(user: UserDetails) {
    this.user = user;
  }

  getUserDetails(): UserDetails | null {
    return this.user;
  }

  isAuthenticated(): boolean {
    return !!this.jwtToken;
  }
}
