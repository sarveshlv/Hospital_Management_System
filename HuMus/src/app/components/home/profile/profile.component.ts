import { Component, Input } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { UpdateUserRequest, UserDetails } from 'src/app/models/user.requests';
import { JwtStorageService } from 'src/app/services/jwt/jwt.storage.service';
import { UserService } from 'src/app/services/user.service';
import { HttpErrorResponse } from '@angular/common/http';
import { NgToastService } from 'ng-angular-popup';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent {
  userForm: FormGroup;
  userDetails: UserDetails;
  isManager: boolean = false;
  isPatient: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private jwtStorageService: JwtStorageService,
    private userService: UserService,
    private toast: NgToastService
  ) {}
  ngOnInit() {
    this.userDetails = this.jwtStorageService.getUserDetails();
    if(this.userDetails.role === 'MANAGER'){
      this.isManager = true;
    }
    if(this.userDetails.role === 'USER'){
      this.isPatient = true;
    }
    this.userForm = this.formBuilder.group({
      firstName: [this.userDetails.firstName, Validators.required],
      lastName: [this.userDetails.lastName, Validators.required],
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
  }

  updateUser() {
    if (this.userForm.valid) {
      if (!this.passwordsMatch()) {
        return;
      }
      const { email, firstName, lastName, password } = this.userForm.value;

      const updateUserRequest: UpdateUserRequest = {
        email,
        firstName,
        lastName,
        password,
      };

      this.userService.updateUser(updateUserRequest).subscribe({
        next: (response: UserDetails) => {
          this.toast.success({
            detail: `Update user with details`,
            summary:
              'Reloading page to update details',
            duration: 3000,
          });
          this.userDetails = response;
        },
        error: (error: HttpErrorResponse) => {
          this.toast.error({
            detail: 'Unable to update user details',
            summary: 'Plz try again later. Redirecting to user page',
            duration: 3000,
          });
        },
        complete: () => window.location.reload()
      });
    } else {
      Object.keys(this.userForm.controls).forEach((field) => {
        const control = this.userForm.get(field);
        if (control instanceof FormControl) {
          control.markAsDirty({ onlySelf: true });
        }
      });
    }
  }

  passwordsMatch(): boolean {
    const password = this.userForm.get('password').value;
    const confirmPassword = this.userForm.get('confirmPassword').value;

    if (password !== confirmPassword) {
      this.userForm.get('confirmPassword').reset();
      this.userForm.get('confirmPassword').markAsDirty();
      return false;
    }
    return true;
  }
}
