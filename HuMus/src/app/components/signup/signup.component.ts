import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AddUserRequest, UserDetails } from 'src/app/models/user.requests';
import { UserService } from '../../services/user.service';
import { NgToastService } from 'ng-angular-popup';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent {
  public signUpForm!: FormGroup;
  addUserRequest: AddUserRequest = {} as AddUserRequest;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router,
    private toast: NgToastService
  ) {}

  ngOnInit() {
    this.signUpForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      role: ['', Validators.required],
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

  onSubmit() {
    if (this.signUpForm.valid) {
      if (!this.passwordsMatch()) {
        return;
      }

      const userRequest = this.createUserRequest();

      this.userService.getUserByEmail(userRequest.email).subscribe({
        next: (response: UserDetails) => {
          this.toast.success({
            detail: `User already present with ${userRequest.email}`,
            summary: 'Please try with a different email id',
            duration: 3000,
          });
          this.signUpForm.reset();
        },
        error: (error: HttpErrorResponse) => {
          this.userService.addUser(userRequest).subscribe({
            next: () => {
              this.toast.info({
                detail: 'User signed up successfully',
                summary: 'You can now log in with your credentials',
                duration: 3000,
              });
              this.signUpForm.reset();
              this.router.navigate(['/login']);
            },
            error: (error: HttpErrorResponse) =>
              this.toast.success({
                detail: 'Unable to sign up!',
                summary: error.error.error,
                duration: 3000,
              }),
          });
        },
      });
    } else {
      Object.keys(this.signUpForm.controls).forEach((field) => {
        const control = this.signUpForm.get(field);
        control.markAsDirty({ onlySelf: true });
      });
    }
  }

  passwordsMatch(): boolean {
    const password = this.signUpForm.get('password').value;
    const confirmPassword = this.signUpForm.get('confirmPassword').value;

    if (password !== confirmPassword) {
      this.signUpForm.get('confirmPassword').reset();
      this.signUpForm.get('confirmPassword').markAsDirty();
      return false;
    }
    return true;
  }

  createUserRequest() {
    return {
      firstName: this.signUpForm.get('firstName').value,
      lastName: this.signUpForm.get('lastName').value,
      email: this.signUpForm.get('email').value,
      password: this.signUpForm.get('password').value,
      role: this.signUpForm.get('role').value,
    };
  }
}
