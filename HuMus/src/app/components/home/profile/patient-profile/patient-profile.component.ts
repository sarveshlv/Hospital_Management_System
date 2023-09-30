import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgToastService } from 'ng-angular-popup';
import { AddPatientRequest, Patient } from 'src/app/models/patient.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { JwtStorageService } from 'src/app/services/jwt/jwt.storage.service';
import { PatientService } from 'src/app/services/patient.service';

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
  selector: 'app-patient-profile',
  templateUrl: './patient-profile.component.html',
  styleUrls: ['./patient-profile.component.css'],
})
export class PatientProfileComponent {

  indianStatesAndUTs: string[] = indianStatesAndUTs;
  patientForm: FormGroup;
  
  userDetails: UserDetails;
  patient: Patient;

  constructor(
    private formBuilder: FormBuilder,
    private jwtStorageService: JwtStorageService,
    private patientService: PatientService,
    private toast: NgToastService
  ) {}

  //fetching and populating hospital details
  ngOnInit() {
    this.userDetails = this.jwtStorageService.getUserDetails();
    this.patientService
      .findPatientById(this.userDetails.referenceId)
      .subscribe({
        next: (response: Patient) => {
          this.patient = response;
          this.toast.info({
            detail: 'Success',
            summary: `${response.firstName} ${response.lastName}'s details fetched successfully`,
            duration: 3000,
          });
        },
        error: (error: HttpErrorResponse) =>
          this.toast.error({
            detail: "Unable to fetch patient's details",
            summary: error.error.error,
            duration: 3000,
          }),
        complete: () => {
          this.patientForm = this.formBuilder.group({
            firstName: [this.patient.firstName, Validators.required],
            lastName: [this.patient.lastName, Validators.required],
            contactNumber: [
              this.patient.contactNumber,
              [Validators.required, Validators.pattern(/[6-9][0-9]{9}/)],
            ],
            aadharNumber: [
              this.patient.aadharCard,
              [Validators.required, Validators.pattern(/[0-9]{12}/)],
            ],
            state: [this.patient.address.state, Validators.required],
            city: [this.patient.address.city, Validators.required],
            pincode: [
              this.patient.address.pincode,
              [Validators.required, Validators.pattern(/[0-9]{6}/)],
            ],
          });
        },
      });
  }

  //click action to update patient details
  updatePatient() {
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

      this.patientService
        .updatePatient(this.patient.id, addPatientRequest)
        .subscribe({
          next: (response: Patient) => {
            this.patient = response;
            this.toast.success({
              detail: 'Patient updated successfully',
              summary: 'Reloading page to update details',
              duration: 3000,
            });
          },
          error: (error: HttpErrorResponse) => {
            this.toast.error({
              detail: 'Unable to update patient. Please try again later',
              summary: error.error.e,
              duration: 3000,
            });
          },
          complete: () => window.location.reload(),
        });
    } else {
      Object.keys(this.patientForm.controls).forEach((field) => {
        const control = this.patientForm.get(field);
        control.markAsDirty({ onlySelf: true });
      });
    }
  }
}
