import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgToastService } from 'ng-angular-popup';
import { AddPatient } from 'src/app/models/patient.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { JwtService } from 'src/app/services/jwt/jwt.service';
import { PatientService } from 'src/app/services/patient.service';
import { UserService } from 'src/app/services/user.service';
import ValidateForm from '../login/login/login.component';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

//Array for populating state names in the select field
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
  selector: 'app-add-patient',
  templateUrl: './add-patient.component.html',
  styleUrls: ['./add-patient.component.css'],
})
export class AddPatientComponent {
  states: string[] = states;
  addPatientForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private patientService: PatientService,
    private jwtService: JwtService,
    private userService: UserService,
    private ngToast: NgToastService,
    private router: Router
  ) {}

  //Validate the form in real time
  ngOnInit() {
    this.addPatientForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      contactNo: [
        '',
        [Validators.required, Validators.pattern(/[6-9][0-9]{9}/)],
      ],
      aadhar: ['', [Validators.required, Validators.pattern(/[0-9]{12}/)]],
      streetAddress: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      pincode: ['', [Validators.required, Validators.pattern(/[0-9]{6}/)]],
    });
  }

  //Call addPatient API on submit
  onSubmit() {
    if (this.addPatientForm.valid) {
      const values = this.addPatientForm.value;

      //Create AddPatient object
      const addPatient: AddPatient = {
        firstName: values.firstName,
        lastName: values.lastName,
        contactNumber: values.contactNo,
        aadharNumber: values.aadhar,
        address: {
          streetAddress: values.streetAddress,
          cityName: values.city,
          stateName: values.state,
        },
        pincode: values.pincode,
      };

      this.patientService.addPatient(addPatient).subscribe(
        (response) => {
          const userDetails: UserDetails = this.jwtService.getUserDetails()!;
          userDetails.referenceId = response.patientId;
          this.jwtService.setUserDetails(userDetails);

          this.userService
            .addReference(userDetails.emailId, response.patientId)
            .subscribe(
              (response) => {
                this.handleSuccess(
                  'Patient information added Successfully',
                  ''
                );
                this.addPatientForm.reset();
                this.router.navigate(['/userhome']);
                // console.log(response);
              },
              (error:HttpErrorResponse) => {
                this.handleError('Error', error.error);
                this.addPatientForm.reset();
              }
            );
        },
        (error:HttpErrorResponse) => {
          this.handleError('Error', error.error);
          this.addPatientForm.reset();
        }
      );
    } else {
      ValidateForm.validateAllFormFields(this.addPatientForm);
    }
  }

  //Handle Success messages 
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
