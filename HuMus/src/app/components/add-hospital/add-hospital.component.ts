import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import ValidateForm from 'src/app/helpers/validate.form';
import {
  AddHospitalRequest,
  HospitalType,
} from 'src/app/models/hospital.requests';
import { NgToastService } from 'ng-angular-popup';

import { HospitalService } from 'src/app/services/hospital.service';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { JwtStorageService } from 'src/app/services/jwt/jwt.storage.service';
import { UserDetails } from 'src/app/models/user.requests';

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
  selector: 'app-add-hospital',
  templateUrl: './add-hospital.component.html',
  styleUrls: ['./add-hospital.component.css'],
})
export class AddHospitalComponent {
  hospitalForm: FormGroup;
  hostpitalTypes: string[] = Object.values(HospitalType);
  indianStatesAndUTs: string[] = indianStatesAndUTs;

  constructor(
    private fb: FormBuilder,
    private hospitalService: HospitalService,
    private userService: UserService,
    private jwtStorageService: JwtStorageService,
    private toast: NgToastService,
    private router: Router
  ) {}

  ngOnInit() {
    this.hospitalForm = this.fb.group({
      name: ['', Validators.required],
      hospitalType: ['', Validators.required],
      contactNo: [
        '',
        [Validators.required, Validators.pattern(/[6-9][0-9]{9}/)],
      ],
      state: ['', Validators.required],
      city: ['', Validators.required],
      pincode: ['', [Validators.required, Validators.pattern(/[0-9]{6}/)]],
    });
  }

  private handleSuccess(message: string, summary: string) {
    this.toast.success({
      detail: message,
      summary: summary,
      duration: 3000,
    });
  }

  private handleError(errorMessage: string, error: any) {
    this.toast.error({
      detail: errorMessage,
      summary: error.message,
      duration: 3000,
    });
  }

  onSubmit() {
    if (this.hospitalForm.valid) {
      const formValues = this.hospitalForm.value;

      // Create AddHospitalRequest object to be send
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
      //request to be send to API
      console.log('Add Hospital Request:', addHospitalRequest);
      this.hospitalService.addHospital(addHospitalRequest).subscribe({
        next: (response) => {
          console.log('Added hospital with details: ', response);
          
          const userDetails: UserDetails = this.jwtStorageService.getUserDetails();
          userDetails.referenceId = response.id;
          this.jwtStorageService.setUserDetails(userDetails);

          console.log('Attaching profile with hospital details: ', userDetails, response);
           
          this.userService.addReference( userDetails.email, response.id).subscribe({
            next: () => {
              this.handleSuccess('Added hospital Successfully', 'Welcome to our awesome dashboard!');
              this.router.navigate(['/dashboard']);
            },
            error: (error) => {
              this.toast.warning({
                detail:"Unable to attach hospital details to ur profile",
                summary: error.message,
                duration: 3000,
              }) 
              this.handleError("Plz try again later", "Apologises for problem caused");
              this.hospitalForm.reset();
            }
          })
        },
        error: (error) => {
          this.handleError('Something went wrong!', error.message);
        this.hospitalForm.reset();
        }
      });
    } else {
      ValidateForm.validateAllFormFields(this.hospitalForm);
    }
  }
}
