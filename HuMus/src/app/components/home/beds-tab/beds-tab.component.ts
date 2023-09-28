import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';
import { Bed } from 'src/app/models/bed.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { BedService } from 'src/app/services/bed.service';
import { JwtStorageService } from 'src/app/services/jwt/jwt.storage.service';

@Component({
  selector: 'app-beds-tab',
  template:
    '<div class="container responsive-grid"><app-bed-item-component *ngFor="let bed of beds"></app-bed-item-component></div>',
  styles: [
    '.container{padding: 24px;} .responsive-grid{display: grid; grid-template-columns: repeat(auto-fill, minmax(250px , 1fr)); gap: 24px;}',
  ],
})
export class BedsTabComponent implements OnInit {
  isManager = false;
  isPatient = false;
  userDetails: UserDetails;
  beds: Bed[];
  bedCount = 0;
  constructor(
    private jwtStorageService: JwtStorageService,
    private bedsService: BedService,
    private toast: NgToastService
  ) {}

  ngOnInit(): void {
    this.userDetails = this.jwtStorageService.getUserDetails();
    this.isManager = this.userDetails.role === 'MANAGER';
    this.isPatient = this.userDetails.role === 'USER';

    this.loadbeds();
  }

  private loadbeds(): void {
    let bedsObservable;

    if (this.isManager) {
      bedsObservable = this.bedsService.getBedsByHospitalId(
        this.userDetails.referenceId
      );
    } else if (this.isPatient) {
      bedsObservable = this.bedsService.getAllBeds();
    }

    if (bedsObservable) {
      bedsObservable.subscribe({
        next: (response: Bed[]) => {
          console.log('Fetched beds:', response);
          this.beds = response;
          // this.applyFilter();
        },
        error: (error: HttpErrorResponse) => {
          this.handleError(
            `Unable to fetch ${this.isManager ? 'hospital' : 'patient'} beds`,
            error.error
          );
          console.log(error.error);
        },
      });
    }
  }

  private handleError(errorMessage: string, error: any) {
    this.toast.error({
      detail: errorMessage,
      summary: error.error,
      duration: 3000,
    });
  }
}
