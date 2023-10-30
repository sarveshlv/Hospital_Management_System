import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { LoginRequest } from 'src/app/models/user.requests';
import { JwtService } from 'src/app/services/jwt/jwt.service';
import { UserService } from 'src/app/services/user.service';
import { NgToastService } from 'ng-angular-popup';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { HospitalService } from 'src/app/services/hospital.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  loginRequest: LoginRequest = {} as LoginRequest;
  loginForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private jwtService: JwtService,
    private ngToast: NgToastService,
    private router: Router,
    private hospitalService: HospitalService
  ) {}

  //Validate form in real time
  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [
          Validators.required,
          // Validators.minLength(8),
          // Validators.pattern(/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#])[a-zA-Z\d@#]+$/),
        ],
      ],
    });
  }

  //Function to process login procedure
  onSubmit() {
    if (this.loginForm.valid) {
      this.loginRequest = {
        email: this.loginForm.get('email')?.value,
        password: this.loginForm.get('password')?.value,
      };

      this.userService.signin(this.loginRequest).subscribe({
        next: (response) => {
          // console.log(response);
          this.successLogin(response);
        },
        error: (error: HttpErrorResponse) => {
          // console.log(error);
          this.handleError('Login failed!', error.error);
        },
      });
    } else {
      ValidateForm.validateAllFormFields(this.loginForm);
    }
  }

  //Function to perform after success login and determine the route for further flow
  private successLogin(response: any) {
    this.jwtService.setToken(response.token);
    // console.log(response.userDetails);
    this.jwtService.setUserDetails(response.userDetails);

    if (response.userDetails.referenceId) {
      this.handleSuccess('Logged in successfully', '');
      if (response.userDetails.role === 'USER') {
        this.router.navigate(['/userhome']);
      } else if (response.userDetails.role === 'MANAGER') {
        this.hospitalService.getHospitalById(response.userDetails.referenceId).subscribe(
          (response) => {
            if(response.status === 'DECLINED') {
              this.ngToast.info({
                detail: 'Attention!',
                summary: 'Your previous registration was declined! Please fill the details correctly again and submit',
                duration: 10000
              })
              this.router.navigate(['/addhospital']);
            } else {
              this.router.navigate(['/managerhome']);
            }
          }
        )
      }
    } else if (response.userDetails.role === 'MANAGER') {
      this.handleSuccess(
        'Logged in successfully',
        'Add Hospital details to continue!'
      );
      this.router.navigate(['/addhospital']);
    } else if (response.userDetails.role === 'USER') {
      this.handleSuccess(
        'Logged in successfully',
        'Add Patient details to continue!'
      );
      this.router.navigate(['/addPatient']);
    } else {
      this.handleSuccess('Logged in successfully', '');
      this.router.navigate(['/adminhome']);
    }
  }

  //Handle Success messages
  private handleSuccess(message: string, summary: string) {
    this.ngToast.success({
      detail: message,
      summary: summary,
      duration: 5000,
    });
    this.loginForm.reset();
  }

  //Handle Error messages
  private handleError(message: string, summary: any) {
    this.ngToast.error({
      detail: message,
      summary: summary.error,
      duration: 5000,
    });
    this.loginForm.reset();
  }
}

//Helper Class to handle Validations for the forms
export default class ValidateForm {
  static validateAllFormFields(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach((field) => {
      const control = formGroup.get(field);
      if (control instanceof FormControl) {
        control.markAsDirty({ onlySelf: true });
      } else if (control instanceof FormGroup) {
        this.validateAllFormFields(control);
      }
    });
  }
}
