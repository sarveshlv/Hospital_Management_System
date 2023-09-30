import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';
import { Hospital } from 'src/app/models/hospital.requests';
import { HospitalService } from 'src/app/services/hospital.service';

@Component({
  selector: 'app-hospitals-tab',
  templateUrl: './hospitals-tab.component.html',
  styleUrls: ['./hospitals-tab.component.css'],
})
export class HospitalsTabComponent implements OnInit {
  hospitals: Hospital[] = [];

  constructor(
    private hospitalService: HospitalService,
    private toast: NgToastService
  ) {}

  ngOnInit(): void {
    this.hospitalService.getAllHospitals().subscribe({
      next: (response: Hospital[]) => {
        this.hospitals = response;
        this.toast.info({
          detail: 'Found registered hositals',
          summary: `Fetched ${this.hospitals.length} hospitals`,
          duration: 3000,
        });
      },
      error: (error: HttpErrorResponse) => {
        this.toast.error({
          detail: 'Unable to fetch registered hositals',
          summary: error.error.error,
          duration: 3000,
        });
      },
    });
  }
  verifyHospital(hospital: Hospital){
    this.hospitalService.verifyHospital(hospital.id).subscribe({
      next: (respone: Hospital) => {
        this.toast.success({
        detail: 'Hospial verifed sucessfully',
        summary: '',
        duration: 3000
      });
      hospital = respone;
      const index = this.hospitals.findIndex(h => h.id === hospital.id);
      if (index !== -1) {
        this.hospitals[index] = hospital;
      }
    },
      error: (error: HttpErrorResponse) => 
      this.toast.error({
        detail: 'Unable to update verfication status',
        summary: error.error.error,
        duration: 3000
      })
    })
  }
}
