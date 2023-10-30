import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import { RegisterUserRequest } from 'src/app/models/user.requests';
import { UserService } from 'src/app/services/user.service';
import ValidateForm from '../login/login/login.component';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent implements OnInit {
  signUpForm!: FormGroup;

  registerUserRequest!: RegisterUserRequest;

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private ngToastSrevice: NgToastService,
    private router: Router
  ) {

  }

  ngOnInit() {
    this.signUpForm = this.formBuilder.group({
      firstName: ['', [Validators.required, Validators.pattern(/[A-Za-z]/)]],
      lastName: ['', Validators.pattern(/[A-za-z]/)],
      email: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@#])[a-zA-Z\d@#]+$/),
        ],
      ],
      confirmpassword: ['', [Validators.required]],
      role: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.signUpForm.get('password')!.value != this.signUpForm.get('confirmpassword')!.value) {
      this.ngToastSrevice.error({
        detail: "Confirm password doesn't match!",
        summary: '',
        duration: 5000,
      });
    } else {
      if (this.signUpForm.valid) {
        this.registerUserRequest = {
          firstName: this.signUpForm.get('firstName')!.value,
          lastName: this.signUpForm.get('lastName')!.value,
          emailId: this.signUpForm.get('email')!.value,
          password: this.signUpForm.get('password')!.value,
          role: this.signUpForm.get('role')!.value,
        };

        this.userService.registerUser(this.registerUserRequest).subscribe({
          next: (response) => {
            // console.log(response);
            this.ngToastSrevice.success({
              detail: 'User added successfuly',
              summary: 'Please log in using your email Id and password!',
              duration: 5000,
            });

            this.signUpForm.reset();
            this.router.navigate(['/login']);
          },
          error: (error: HttpErrorResponse) => {
            // console.log(error);
            this.ngToastSrevice.error({
              detail: "Sorry! Coudn't register",
              summary: error.error.error,
              duration: 8000,
            });
            this.signUpForm.reset();
          },
        });
      } else {
        ValidateForm.validateAllFormFields(this.signUpForm);
      }
    }
  }
}
