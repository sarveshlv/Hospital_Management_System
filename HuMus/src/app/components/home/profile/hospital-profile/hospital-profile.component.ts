import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgToastService } from 'ng-angular-popup';
import { AddHospitalRequest, Hospital } from 'src/app/models/hospital.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { HospitalService } from 'src/app/services/hospital.service';
import { JwtStorageService } from 'src/app/services/jwt/jwt.storage.service';

const indianStatesAndUTs: string[] = [
  'Andhra Pradesh',
  'Arunachal Pradesh',
  'Assam',
  'Bihar',
  'Chhattisgarh',
  'Goa',
  'Gujarat',
  'Haryana',
  'Himachal Pradesh',
  'Jharkhand',
  'Karnataka',
  'Kerala',
  'Madhya Pradesh',
  'Maharashtra',
  'Manipur',
  'Meghalaya',
  'Mizoram',
  'Nagaland',
  'Odisha',
  'Punjab',
  'Rajasthan',
  'Sikkim',
  'Tamil Nadu',
  'Telangana',
  'Tripura',
  'Uttar Pradesh',
  'Uttarakhand',
  'West Bengal',
  'Andaman and Nicobar Islands',
  'Chandigarh',
  'Dadra and Nagar Haveli and Daman and Diu',
  'Lakshadweep',
  'Delhi',
  'Puducherry',
];

@Component({
  selector: 'app-hospital-profile',
  templateUrl: './hospital-profile.component.html',
  styleUrls: ['./hospital-profile.component.css'],
})
export class HospitalProfileComponent {
  hospitalForm: FormGroup;
  userDetails: UserDetails;
  hospital: Hospital;
  indianStatesAndUTs: string[] = indianStatesAndUTs;


  constructor(
    private formBuilder: FormBuilder,
    private jwtStorageService: JwtStorageService,
    private hospitalService: HospitalService, // Adjust the service import
    private toast: NgToastService
  ) {}

  ngOnInit() {
    this.userDetails = this.jwtStorageService.getUserDetails();
    this.hospitalService
      .getHospitalById(this.userDetails.referenceId)
      .subscribe({
        next: (respone: Hospital) => {
          this.hospital = respone;
          this.toast.info({
            detail: 'Success',
            summary: `${respone.name} details fetched successfully`,
            duration: 3000,
          });
        },
        error: (error: HttpErrorResponse) => 
        this.toast.error({
          detail: "Unable to ur hospital's details",
          summary: error.error.error,
          duration: 3000
        }),
        complete: () =>  {
          this.hospitalForm = this.formBuilder.group({
            name: [this.hospital.name, Validators.required],
            hospitalType: [this.hospital.hospitalType, Validators.required],
            contactNo: [
              this.hospital.contactNo,
              [Validators.required, Validators.pattern(/[6-9][0-9]{9}/)],
            ],
            state: [this.hospital.address.state, Validators.required],
            city: [this.hospital.address.city, Validators.required],
            pincode: [this.hospital.address.pincode, [Validators.required, Validators.pattern(/[0-9]{6}/)]],
          });
        }
      });
    
  }

  updateHospital() {
    if (this.hospitalForm.valid) {
      const formValues = this.hospitalForm.value;

      const addHospitalRequest: AddHospitalRequest = {
        name: formValues.name,
        hospitalType: formValues.hospitalType,
        contactNo: formValues.contactNo,
        address: {
          city: formValues.city,
          state: formValues.state,
          pincode: formValues.pincode,
        },
      };
      
      this.hospitalService.updateHospital(this.hospital.id,addHospitalRequest).subscribe({
        next: (respone: Hospital) => {
          this.hospital = respone;
          this.toast.success({
            detail: 'Hospital updated successfully',
            summary: 'Reloading page to update details',
            duration: 3000,
          });
        },
        error: (error: HttpErrorResponse) => {
          this.toast.error({
            detail: 'Unable to update hospital',
            summary: 'Please try again later',
            duration: 3000,
          });
        },
        complete: () => window.location.reload()
      });
    } else {
      Object.keys(this.hospitalForm.controls).forEach((field) => {
        const control = this.hospitalForm.get(field);
        control.markAsDirty({ onlySelf: true });
      });
    }
  }
}
