import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';
import { UserDetails } from 'src/app/models/user.requests';
import { JwtStorageService } from 'src/app/services/jwt/jwt.storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  
  isAdmin: boolean = false;
  
  showHospitals = false;
  showBookings = false;
  showBeds = false;
  showProfileToggle = false;

  constructor(private jwtStorageService: JwtStorageService, private userService: UserService, private toast: NgToastService) {}

  //setting up nav bars
  ngOnInit(): void {
    if (this.jwtStorageService.getUserDetails().role === 'MANAGER') {
      this.showBookings = true;
    } else if (this.jwtStorageService.getUserDetails().role === 'USER') {
      this.showBeds = true;
    } else if (this.jwtStorageService.getUserDetails().role === 'ADMIN') {
      this.isAdmin = true;
      this.showHospitals = true;
    }
  }

  //closing views
  closeAll() {
    this.showProfileToggle = false;
    this.showBeds = false;
    this.showBookings = false;
    this.showHospitals = false;
  }

  //logging out
  signOut() {
    this.userService.logout(this.jwtStorageService.getUserDetails().email).subscribe({
      next: (response: UserDetails) => this.toast.success({
        detail: "Logged out successfully",
        duration: 3000
      }),
      error: (error: HttpErrorResponse) => this.toast.error({
        detail: "Unable to log out",
        summary: "Plz try again later",
        duration: 3000
      }),
      complete: () => this.jwtStorageService.clearToken()
    });
    location.reload();
  }
}
