import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
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
    private router: Router,
    private jwtService: JwtStorageService,
    private toast: NgToastService
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
        error: (error: HttpErrorResponse) => {
          this.toast.error({
            detail: 'Unable to log in',
            summary: error.error.error,
            duration: 3000,
          });
          this.loginForm.reset();
        },
      });
    } else {
      Object.keys(this.loginForm.controls).forEach((field) => {
        const control = this.loginForm.get(field);
        control.markAsDirty({ onlySelf: true });
      });
    }
  }

  private handleLoginSuccess(response: any) {
    console.log(response);
    this.jwtService.setToken(response.token);
    this.jwtService.setUserDetails(response.userDetails);

    const userDetails: UserDetails = this.jwtService.getUserDetails();
    if (userDetails.referenceId || userDetails.role === 'ADMIN') {
      this.toast.success({
        detail: 'Logged in successfully',
        summary: 'Welcome to the our awesome dashboard!',
        duration: 3000,
      });
      this.router.navigate(['/dashboard']);
    } else if (userDetails.role === 'MANAGER') {
      this.toast.success({
        detail: 'Logged in successfully',
        summary: 'Add Hospital details to continue!',
        duration: 3000,
      });
      this.router.navigate(['/addHospital']);
    } else if (userDetails.role === 'USER') {
      this.toast.success({
        detail: 'Logged in successfully',
        summary: 'Add Patient details to continue!',
        duration: 3000,
      });
      this.router.navigate(['/addPatient']);
    }
  }

  findEmailById() {
    const email: string = this.findEmailForm.get('email').value;
    this.userService.getUserByEmail(email).subscribe({
      next: (response) => {
        console.log('User found for email id: ', response);
        this.isEmailFound = true;
        this.toast.success({
          detail: 'User found for email id',
          summary: email,
          duration: 3000,
        });
      },
      error: (error: HttpErrorResponse) => {
        this.toast.error({
          detail: `User not found for email id: ${email}`,
          summary: error.error.error,
          duration: 3000,
        });
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
          this.toast.success({
            detail: 'Password reset successfully',
            summary: 'Log in with new credentials',
            duration: 3000,
          });
          window.location.reload();
        },
        error: (error: HttpErrorResponse) =>
          this.toast.success({
            detail: 'Something went wrong. Unable to reset password',
            summary: error.error.error,
            duration: 3000,
          }),
      });
    } else {
      Object.keys(this.resetPasswordForm.controls).forEach((field) => {
        const control = this.resetPasswordForm.get(field);
        control.markAsDirty({ onlySelf: true });
      });
    }
  }
}
