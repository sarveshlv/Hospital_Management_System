import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import ValidateForm from 'src/app/helpers/validate.form';
import { AddUserRequest } from 'src/app/models/user.requests';
import { UserService } from '../../services/user.service';
import { NgToastService } from 'ng-angular-popup';
import { AppRoutingModule } from 'src/app/app-routing.module';
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
    private toast: NgToastService,
    private router: Router
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
        next: (response) => {
          this.handleError(
            `User already present with ${userRequest.email}`,
            'Please try with a different email id'
          );
          this.signUpForm.reset();
        },
        error: (error) => {
          this.userService.addUser(userRequest).subscribe({
            next: () => {
              this.handleSuccess(
                'User signed up successfully',
                'You can now log in with your credentials'
              );
              this.signUpForm.reset();
              this.router.navigate(['/login']); // open welcome component
            },
            error: (error: HttpErrorResponse) =>
              this.handleError('Unable to sign up!', error.error),
          });
        },
      });
    } else {
      ValidateForm.validateAllFormFields(this.signUpForm);
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
}
