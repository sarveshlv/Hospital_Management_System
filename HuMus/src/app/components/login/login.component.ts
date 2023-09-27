import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import ValidateForm from 'src/app/helpers/validate.form';
import {
  LoginRequest,
  UpdatePasswordRequest,
  UserDetails,
} from 'src/app/models/user.requests';
import { JwtStorageService } from 'src/app/services/jwt/jwt.storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  loginRequest: LoginRequest = {} as LoginRequest;
  updatePasswordRequest: UpdatePasswordRequest = {} as UpdatePasswordRequest;
  isEmailFound: boolean = false;

  loginForm!: FormGroup;
  findEmailForm!: FormGroup;
  resetPasswordForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private toast: NgToastService,
    private router: Router,
    private jwtService: JwtStorageService
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#])[a-zA-Z\d@#]+$/),
        ],
      ],
    });

    this.resetPasswordForm = this.fb.group({
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#])[a-zA-Z\d@#]+$/),
        ],
      ],
      confirmPassword: ['', Validators.required],
    });
    this.findEmailForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  private handleSuccess(message: string, summary: string) {
    this.toast.success({
      detail: message,
      summary: summary,
      duration: 3000,
    });
    this.loginForm.reset();
    this.resetPasswordForm.reset();
  }

  private handleError(errorMessage: string, error: any) {
    this.toast.error({
      detail: errorMessage,
      summary: error.message,
      duration: 3000,
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.loginRequest = {
        email: this.loginForm.get('email').value,
        password: this.loginForm.get('password').value,
      };
      this.userService.login(this.loginRequest).subscribe({
        next: (response) => {
          this.handleLoginSuccess(response);
        },
        error: (error) => {
          this.handleError('Bad credentials! Unable to log in', error);
          this.loginForm.reset();
        }
      });
    } else {
      ValidateForm.validateAllFormFields(this.loginForm);
    }
  }

  private handleLoginSuccess(response: any) {
    console.log(response);
    this.jwtService.setToken(response.token);
    this.jwtService.setUserDetails(response.userDetails);

    const userDetails: UserDetails = this.jwtService.getUserDetails();
    if (userDetails.referenceId || userDetails.role === 'ADMIN') {
      this.handleSuccess('Logged in successfully', 'Welcome to the out awesome dashboard!');
      this.router.navigate(['/dashboard']);
    } else if (userDetails.role === 'MANAGER') {
      this.handleSuccess('Logged in successfully', 'Add Hospital details to continue!');
      this.router.navigate(['/addHospital']);
    } else if (userDetails.role === 'USER') {
      this.handleSuccess('Logged in successfully', 'Add Patient details to continue!');
      this.router.navigate(['/addPatient']);
    }

  }

  findEmailById() {
    const email: string = this.findEmailForm.get('email').value;
    this.userService.getUserByEmail(email).subscribe({
      next: (response) => {
        console.log('User found for email id: ', response);
        this.isEmailFound = true;
        this.handleSuccess('User found for email id', email);
      },
      error: (error) => {
        this.handleError(`User not found for email id: ${email}`, error);
        this.findEmailForm.reset();
        this.isEmailFound = false;
      },
    });
  }

  resetPassword() {
    this.updatePasswordRequest = {
      email: this.findEmailForm.get('email').value,
      password: this.resetPasswordForm.get('password').value,
    };
    if (this.resetPasswordForm.valid) {
      this.userService.updatePassword(this.updatePasswordRequest).subscribe({
        next: (response) => {
          console.log('Password updated with details: ', response);
          this.isEmailFound = false;
          this.handleSuccess('Password reset successfully', 'Log in with new credentials');
          window.location.reload();
        },
        error: (error) =>
          this.handleError('Something went wrong. Unable to reset password', error),
      });
    } else {
      ValidateForm.validateAllFormFields(this.resetPasswordForm);
    }
  }
}