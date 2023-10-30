import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserDetails } from 'src/app/models/user.requests';
import { JwtService } from 'src/app/services/jwt/jwt.service';

@Component({
  selector: 'app-userhome',
  templateUrl: './userhome.component.html',
  styleUrls: ['./userhome.component.css'],
})
export class UserhomeComponent {
  loggedInUserName: string = '';
  userDetails!: UserDetails;
  routeName: string = '';

  constructor(private router: Router, private jwtService: JwtService) {}

  ngOnInit() {
    this.userDetails = this.jwtService.getUserDetails()!;
    this.loggedInUserName = this.userDetails.firstName;
  }

  checkRoute(route: string) {
    this.routeName = route;
  }

  //Find out the router path based on the user details(role)
  isBookBedRoute(): boolean {
    if (this.routeName === 'Book') {
      return this.router.url.includes('/userhome/bookbed');
    } else if (this.routeName === 'History') {
      return this.router.url.includes('/userhome/history');
    } else {
      return this.router.url.includes('/userhome/status');
    }
  }
}
