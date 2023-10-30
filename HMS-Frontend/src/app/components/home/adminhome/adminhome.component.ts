import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';
import { Hospital } from 'src/app/models/hospital.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { HospitalService } from 'src/app/services/hospital.service';
import { JwtService } from 'src/app/services/jwt/jwt.service';

@Component({
  selector: 'app-adminhome',
  templateUrl: './adminhome.component.html',
  styleUrls: ['./adminhome.component.css'],
})
export class AdminhomeComponent {
  loggedInUserName: string = '';
  userDetails!: UserDetails;
  newHospitals: Hospital[] = [];
  oldHospitals: Hospital[] = [];

  showNew = true;
  showOld = false;

  constructor(
    private jwtService: JwtService,
    private hospitalService: HospitalService,
    private ngToast: NgToastService
  ) {}

  //Get hospitals on loading component
  ngOnInit() {
    this.userDetails = this.jwtService.getUserDetails()!;
    this.loggedInUserName = this.userDetails.firstName;
    this.hospitalService.getAllHospitals().subscribe(
      (response) => {
        this.newHospitals = response.filter((hospital) => {
          return hospital.status === 'REQUESTED';
        });
      },
      (error: HttpErrorResponse) => {
        this.handleError('Error', error.error);
      }
    );
  }

  // Load Already registered Hospitals
  loadHospitals() {
    this.hospitalService.getAllHospitals().subscribe((response) => {
      this.oldHospitals = response.filter((hospital) => {
        return hospital.status === 'APPROVED';
      });
    });
  }

  // Approve the newly registered Hospitals
  approveHospital(hospital: Hospital) {
    this.hospitalService
      .approveDeclineRegistration(hospital.hospitalId, 'APPROVE')
      .subscribe((response) => {
        this.handleSuccess('Approved!', '');
        this.ngOnInit();
      });
  }

  // Decline the newly registered Hospitals
  declineHospital(hospital: Hospital) {
    this.hospitalService
      .approveDeclineRegistration(hospital.hospitalId, 'REJECT')
      .subscribe((response) => {
        this.ngToast.info({
          detail: 'Declined Registration!',
          summary: '',
          duration: 5000,
        });
        this.ngOnInit();
      });
  }

  //Handle Success messages
  handleSuccess(msg: string, summary: string) {
    this.ngToast.success({
      detail: msg,
      summary: summary,
      duration: 5000,
    });
  }

  //Handle Error messages
  handleError(msg: string, error: any) {
    this.ngToast.error({
      detail: msg,
      summary: error.error,
      duration: 5000,
    });
  }
}
