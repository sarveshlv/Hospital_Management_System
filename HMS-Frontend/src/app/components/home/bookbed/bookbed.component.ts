import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import { Observable } from 'rxjs';
import { Hospital } from 'src/app/models/hospital.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { HospitalService } from 'src/app/services/hospital.service';
import { JwtService } from 'src/app/services/jwt/jwt.service';

@Component({
  selector: 'app-bookbed',
  templateUrl: './bookbed.component.html',
  styleUrls: ['./bookbed.component.css'],
})
export class BookbedComponent implements OnInit {
  selectedOption: string = '';
  searchOption: string = '';
  searchText: string = '';
  pincode!: number;
  hospitalNames: string[] = [];
  filteredNames: string[] = [];

  selectedHospital!: Hospital;
  userDetails!: UserDetails;
  hospitals: Hospital[] = [];
  filteredHospitals: Hospital[] = [];

  constructor(
    private hospitalService: HospitalService,
    private ngToast: NgToastService
  ) {}

  //Load filtered (Approved) hospitals to display for booking
  ngOnInit() {
    this.hospitalService.getAllHospitals().subscribe(
      (response) => {
        this.hospitals = response.filter((hospital) => {
          return hospital.status === 'APPROVED';
        });
        this.filteredHospitals = this.hospitals;
        this.hospitalNames = this.hospitals.map(
          (hospital) => hospital.hospitalName
        );
      },
      (error: HttpErrorResponse) => {
        this.handleError('Error', error.error);
      }
    );
  }

  //Function for processing search and filter functions
  applyFilter() {
    if (this.searchOption === 'Hospital') {
      this.filteredHospitals = this.hospitals.filter((hospital) =>
        hospital.hospitalName
          .toLowerCase()
          .includes(this.searchText.toLowerCase())
      );
    } else if (this.searchOption === 'Pincode') {
      this.hospitalService.findNearbyHospitals(this.pincode).subscribe(
        (response) => {
          this.filteredHospitals = response;
          this.ngToast.info({
            detail: 'Results shown nearest to your location!',
            summary: '',
            duration: 5000,
          });
        },
        (error: HttpErrorResponse) => {
          this.handleError('Error', error.error);
        }
      );
    }
  }

  //Function for pupulating hospital names for input field in search modal
  filterNames() {
    this.filteredNames = this.hospitalNames.filter((name) =>
      name.toLowerCase().includes(this.searchText.toLowerCase())
    );
  }

  //Selecting the input from dropdown
  selectName(name: string) {
    this.searchText = name;
    this.filteredNames = [];
  }

  //Assign the selected hospital
  onViewBedsClick(hospital: Hospital) {
    this.selectedHospital = hospital;
  }

  //Handle Error messages
  private handleError(detail: string, summary: any) {
    this.ngToast.error({
      detail: detail,
      summary: summary.error,
      duration: 5000,
    });
  }
}
