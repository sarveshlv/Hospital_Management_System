import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgToastModule } from 'ng-angular-popup';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { JwtInterceptor } from './jwt.interceptor';
import { AppComponent } from './app.component';
import { SignupComponent } from './components/signup/signup.component';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/home/dashboard/dashboard.component';
import { AddHospitalComponent } from './components/add-hospital/add-hospital.component';
import { AddPatientComponent } from './components/add-patient/add-patient.component';
import { BookingsTabComponent } from './components/home/bookings-tab/bookings-tab.component';
import { BedsTabComponent } from './components/home/beds-tab/beds-tab.component';
import { BookingItemComponent } from './components/home/bookings-tab/booking-item/booking-item.component';
import { BedItemComponentComponent } from './components/home/beds-tab/bed-item-component/bed-item-component.component';
import { ProfileComponent } from './components/home/profile/profile.component';
import { PatientProfileComponent } from './components/home/profile/patient-profile/patient-profile.component';
import { HospitalProfileComponent } from './components/home/profile/hospital-profile/hospital-profile.component';
import { HospitalsTabComponent } from './components/home/dashboard/hospitals-tab/hospitals-tab.component';

@NgModule({
  declarations: [
    AppComponent,
    SignupComponent,
    LoginComponent,
    DashboardComponent,
    AddHospitalComponent,
    AddPatientComponent,
    BookingsTabComponent,
    BedsTabComponent,
    BookingItemComponent,
    BedItemComponentComponent,
    ProfileComponent,
    PatientProfileComponent,
    HospitalProfileComponent,
    HospitalsTabComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgToastModule,
    MatCardModule,
    MatButtonModule,
    MatTooltipModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule,
    BrowserAnimationsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}