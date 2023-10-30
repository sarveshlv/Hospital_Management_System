import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgToastService } from 'ng-angular-popup';
import {
  AddHospitalRequest,
  HospitalType,
} from 'src/app/models/hospital.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { HospitalService } from 'src/app/services/hospital.service';
import { JwtService } from 'src/app/services/jwt/jwt.service';
import { UserService } from 'src/app/services/user.service';
import ValidateForm from '../login/login/login.component';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

const states: string[] = [
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
  selector: 'app-addhospital',
  templateUrl: './addhospital.component.html',
  styleUrls: ['./addhospital.component.css'],
})
export class AddhospitalComponent {
  states: string[] = states;
  hospitalTypes: string[] = Object.values(HospitalType);
  hospitalForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private hospitalService: HospitalService,
    private jwtService: JwtService,
    private userService: UserService,
    private ngToast: NgToastService,
    private router: Router
  ) {}

  ngOnInit() {
    this.hospitalForm = this.formBuilder.group({
      hospitalName: ['', [Validators.required, Validators.pattern(/[A-Za-z]/)]],
      streetAddress: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      pincode: ['', [Validators.required, Validators.pattern(/[0-9]{6}/)]],
      hospitalType: ['', Validators.required],
      contactNo: [
        '',
        [Validators.required, Validators.pattern(/[6-9][0-9]{9}/)],
      ],
    });
  }

  onSubmit() {
    if (this.hospitalForm.valid) {
      const values = this.hospitalForm.value;

      //Create AddHospital object
      const addHospital: AddHospitalRequest = {
        hospitalName: values.hospitalName,
        hospitalType: values.hospitalType,
        contactNo: values.contactNo,
        hospitalAddress: {
          streetAddress: values.streetAddress,
          cityName: values.city,
          stateName: values.state,
        },
        pincode: values.pincode,
      };

      //Call Register Hospital API 
      this.hospitalService.registerHospital(addHospital).subscribe({
        next: (response) => {
          const userDetails: UserDetails = this.jwtService.getUserDetails()!;
          userDetails.referenceId = response.hospitalId;
          this.jwtService.setUserDetails(userDetails);

          this.userService
            .addReference(userDetails.emailId, response.hospitalId)
            .subscribe({
              next: () => {
                this.handleSuccess('Hospital Registered Successfully', '');
                this.hospitalForm.reset();
                this.router.navigate(['/managerhome']);
              },
              error: (error: HttpErrorResponse) => {
                this.handleError('Error', error.error);
                this.hospitalForm.reset();
              },
            });
        },
      });
    } else {
      ValidateForm.validateAllFormFields(this.hospitalForm);
    }
  }

  //Handle Success Messages
  private handleSuccess(detail: string, summary: string) {
    this.ngToast.success({
      detail: detail,
      summary: summary,
      duration: 5000,
    });
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
