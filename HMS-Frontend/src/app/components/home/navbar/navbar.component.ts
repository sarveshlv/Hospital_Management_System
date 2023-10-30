import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';
import { Hospital, UpdateHospital } from 'src/app/models/hospital.requests';
import { AddPatient, Patient } from 'src/app/models/patient.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { HospitalService } from 'src/app/services/hospital.service';
import { JwtService } from 'src/app/services/jwt/jwt.service';
import { PatientService } from 'src/app/services/patient.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  @Input() userName: string | undefined;

  link: string = '';
  userDetails!: UserDetails;
  isManager: boolean = false;
  isPatient: boolean = false;

  patientDetails!: Patient;
  hospitalDetails!: Hospital;

  constructor(
    private jwtService: JwtService,
    private userService: UserService,
    private patientService: PatientService,
    private hospitalService: HospitalService,
    private ngToast: NgToastService
  ) {}

  ngOnInit() {
    this.userDetails = this.jwtService.getUserDetails()!;
    if (this.userDetails.role === 'USER') {
      this.link = '/userhome';
      this.isPatient = true;
      this.patientService
        .findPatient(this.userDetails.referenceId)
        .subscribe((response) => {
          this.patientDetails = response;
        });
    } else if (this.userDetails.role === 'MANAGER') {
      this.link = '/managerhome';
      this.isManager = true;
      this.hospitalService
        .getHospitalById(this.userDetails.referenceId)
        .subscribe((response) => {
          this.hospitalDetails = response;
        });
    } else {
      this.link = '/adminhome';
    }
  }

  clickUpdateProfile() {
    if (this.isPatient) {
      const updatePatient: AddPatient = {
        firstName: this.patientDetails.firstName,
        lastName: this.patientDetails.lastName,
        contactNumber: this.patientDetails.contactNumber,
        aadharNumber: this.patientDetails.aadharCard,
        address: {
          streetAddress: this.patientDetails.address.streetAddress,
          cityName: this.patientDetails.address.cityName,
          stateName: this.patientDetails.address.stateName,
        },
        pincode: this.patientDetails.pincode,
      };

      this.patientService
        .updatePatient(this.patientDetails.patientId, updatePatient)
        .subscribe(
          (response) => {
            this.handleSuccess('Patient Details updated successfully!', '');
          },
          (error: HttpErrorResponse) => {
            this.handleError("Couldn't update patient details!", error.error);
          }
        );
    } else if (this.isManager) {
      const updateHospital: UpdateHospital = {
        contactNo: this.hospitalDetails.contactNo,
        hospitalAddress: {
          streetAddress: this.hospitalDetails.hospitalAddress.streetAddress,
          cityName: this.hospitalDetails.hospitalAddress.cityName,
          stateName: this.hospitalDetails.hospitalAddress.stateName,
        },
        pincode: this.hospitalDetails.pincode,
      };

      this.hospitalService
        .updateHospital(this.userDetails.referenceId, updateHospital)
        .subscribe((response) => {
          this.handleSuccess('Hospital Details updated successfully!', '');
        });
    }
  }

  logout() {
    this.userService.logout(this.userDetails.emailId).subscribe(
      (response) => {
        this.jwtService.clearToken();
        location.reload();
      },
      (error: HttpErrorResponse) => {
        this.handleError(
          "Sorry! Coudn't logout.",
          error.error
        );
      }
    );
  }

  handleSuccess(msg: string, summary: string) {
    this.ngToast.success({
      detail: msg,
      summary: summary,
      duration: 5000,
    });
  }

  handleError(msg: string, error: any) {
    this.ngToast.error({
      detail: msg,
      summary: error.error,
      duration: 5000,
    });
  }
}
