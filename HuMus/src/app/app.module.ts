import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SignupComponent } from './components/signup/signup.component';
import { LoginComponent } from './components/login/login.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgToastModule } from 'ng-angular-popup';
import { DashboardComponent } from './components/home/dashboard/dashboard.component';
import { AddHospitalComponent } from './components/add-hospital/add-hospital.component';
import { AddPatientComponent } from './components/add-patient/add-patient.component';
import { BookingsTabComponent } from './components/home/bookings-tab/bookings-tab.component';
import { BedsTabComponent } from './components/home/beds-tab/beds-tab.component';
import { JwtInterceptor } from './jwt.interceptor';
import { BookingItemComponent } from './components/home/bookings-tab/booking-item/booking-item.component';
import { MatButtonModule } from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import { BedItemComponentComponent } from './components/home/beds-tab/bed-item-component/bed-item-component.component';



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
    BedItemComponentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgToastModule,
    MatCardModule,
    MatButtonModule
  ],
  providers: [        
    { 
      provide: HTTP_INTERCEPTORS, 
      useClass: JwtInterceptor, 
      multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
