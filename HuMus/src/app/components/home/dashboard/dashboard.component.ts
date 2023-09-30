import { Component, OnInit } from '@angular/core';
import { JwtStorageService } from 'src/app/services/jwt/jwt.storage.service';

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

  constructor(private jwtStorageService: JwtStorageService) {}

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
    this.jwtStorageService.clearToken();
    location.reload();
  }
}
