import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import { AddPatientRequest } from 'src/app/models/patient.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { JwtStorageService } from 'src/app/services/jwt/jwt.storage.service';
import { PatientService } from 'src/app/services/patient.service';
import { UserService } from 'src/app/services/user.service';

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
  selector: 'app-add-patient',
  templateUrl: './add-patient.component.html',
  styleUrls: ['./add-patient.component.css'],
})
export class AddPatientComponent {
  patientForm: FormGroup;
  indianStatesAndUTs: string[] = indianStatesAndUTs;

  constructor(
    private fb: FormBuilder,
    private patientService: PatientService,
    private userService: UserService,
    private jwtStorageService:JwtStorageService,
    private toast: NgToastService,
    private router: Router
  ) {}

  ngOnInit() {
    this.patientForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      contactNumber: ['', [Validators.required, Validators.pattern(/[6-9][0-9]{9}/)]],
      aadharNumber: ['', [Validators.required, Validators.pattern(/[0-9]{12}/)]],
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
      summary: error.error,
      duration: 3000,
    });
  }

  onSubmit() {
    if (this.patientForm.valid) {
      const formValues = this.patientForm.value;

      // Create AddPatientRequest object to be sent
      const addPatientRequest: AddPatientRequest = {
        firstName: formValues.firstName,
        lastName: formValues.lastName,
        contactNumber: formValues.contactNumber,
        aadharNumber: formValues.aadharNumber,
        address: {
          city: formValues.city,
          state: formValues.state,
          pincode: formValues.pincode,
        },
      };

      // Request to be sent to API
      console.log('Add Patient Request:', addPatientRequest);

      this.patientService.addPatient(addPatientRequest).subscribe(
        (response) => {
          console.log('Added patient with details: ', response);
          
          const userDetails: UserDetails = this.jwtStorageService.getUserDetails();
          userDetails.referenceId = response.id;
          this.jwtStorageService.setUserDetails(userDetails);

          console.log('Attaching profile with profile details: ', userDetails, response);
           
          this.userService.addReference( userDetails.email, response.id).subscribe({
            next: () => {
              this.handleSuccess('Added patient Successfully', 'Welcome to our awesome dashboard!');
              this.router.navigate(['/dashboard']);
            },
            error: (error: HttpErrorResponse) => {
              this.toast.warning({
                detail:"Unable to attach patient details to ur profile",
                summary: error.error,
                duration: 3000,
              }) 
              this.handleError("Plz try again later", "Apologises for problem caused");
              this.patientForm.reset();
            }
          })
        },
        (error: HttpErrorResponse) => {
          this.handleError('Something went wrong!', error.error);
          this.patientForm.reset();
        }
      );
    } else {
      Object.keys(this.patientForm.controls).forEach((field) => {
        const control = this.patientForm.get(field);
        control.markAsDirty({ onlySelf: true });
      });
    }
  }
}
