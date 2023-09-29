import { Component, OnInit } from '@angular/core';
import { JwtStorageService } from 'src/app/services/jwt/jwt.storage.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  showBookings = false;
  showBeds = false;
  showProfileToggle = false;

  constructor(private jwtStorageService: JwtStorageService) {}

  ngOnInit(): void {
    if (this.jwtStorageService.getUserDetails().role === 'MANAGER') {
      this.showBookings = true;
    } else if (this.jwtStorageService.getUserDetails().role === 'USER') {
      this.showBeds = true;
    }
  }
  showProfile(){
    this.showProfileToggle = true;
  }
  signOut(){
    this.jwtStorageService.clearToken();
    location.reload();
  }
}
