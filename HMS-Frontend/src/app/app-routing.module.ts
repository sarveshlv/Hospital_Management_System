import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { AddhospitalComponent } from './components/addhospital/addhospital.component';
import { AuthGuard } from './auth.guard';
import { AddPatientComponent } from './components/add-patient/add-patient.component';
import { UserhomeComponent } from './components/home/userhome/userhome.component';
import { BookbedComponent } from './components/home/bookbed/bookbed.component';
import { StatusComponent } from './components/home/status/status.component';
import { HistoryComponent } from './components/home/history/history.component';
import { ManagerhomeComponent } from './components/home/managerhome/managerhome.component';
import { AdminhomeComponent } from './components/home/adminhome/adminhome.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  {
    path: 'addhospital',
    component: AddhospitalComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'addPatient',
    component: AddPatientComponent,
    canActivate: [AuthGuard],
  },
  {
    path: 'userhome',
    component: UserhomeComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'bookbed', component: BookbedComponent },
      { path: 'status', component: StatusComponent },
      { path: 'history', component: HistoryComponent },
    ],
  },
  {
    path: 'managerhome',
    canActivate: [AuthGuard],
    component: ManagerhomeComponent,
  },
  {
    path: 'adminhome',
    canActivate: [AuthGuard],
    component: AdminhomeComponent,
  },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
