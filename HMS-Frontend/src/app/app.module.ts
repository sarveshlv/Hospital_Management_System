import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgToastModule } from 'ng-angular-popup';
import { AddhospitalComponent } from './components/addhospital/addhospital.component';
import { AddPatientComponent } from './components/add-patient/add-patient.component';
import { NavbarComponent } from './components/home/navbar/navbar.component';
import { UserhomeComponent } from './components/home/userhome/userhome.component';
import { BookbedComponent } from './components/home/bookbed/bookbed.component';
import { BedInfoComponent } from './components/home/bed-info/bed-info.component';
import { JwtInterceptor } from './jwt.interceptor';
import { StatusComponent } from './components/home/status/status.component';
import { HistoryComponent } from './components/home/history/history.component';
import { ManagerhomeComponent } from './components/home/managerhome/managerhome.component';
import { AdminhomeComponent } from './components/home/adminhome/adminhome.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    AddhospitalComponent,
    AddPatientComponent,
    NavbarComponent,
    UserhomeComponent,
    BookbedComponent,
    BedInfoComponent,
    StatusComponent,
    HistoryComponent,
    ManagerhomeComponent,
    AdminhomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgToastModule
  ],
  providers: [{

    provide: HTTP_INTERCEPTORS,

    useClass: JwtInterceptor,

    multi: true,

  },],
  bootstrap: [AppComponent]
})
export class AppModule { }
