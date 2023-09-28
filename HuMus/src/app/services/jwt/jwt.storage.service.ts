import { Injectable, signal } from '@angular/core';
import { UserDetails } from 'src/app/models/user.requests';

@Injectable({
  providedIn: 'root',
})
export class JwtStorageService {
  private jwtToken: string | null = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0aW1wdS5tYW42OTQyMEBnbWFpbC5jb20iLCJyb2xlcyI6WyJNQU5BR0VSIl0sImlhdCI6MTY5NTg3NDUwMywiZXhwIjoxNjk1ODgxNzAzfQ.NWYQYByF3F7Xr_ghN9BjXbMR4_4prkGlPVwIjGyahFM";
  private userDetails: UserDetails | null = {
    "id": "6513a5e135cf236977e22948",
    "email": "timpu.man69420@gmail.com",
    "firstName": "Prateek",
    "lastName": "Singh",
    "role": "MANAGER",
    "referenceId": "650edf92c3076f1dd19e826e"
};

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
