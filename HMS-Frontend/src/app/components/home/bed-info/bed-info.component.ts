import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import { Bed, BedType } from 'src/app/models/bed.requests';
import { AddBookingRequest } from 'src/app/models/booking.requests';
import { Patient } from 'src/app/models/patient.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { BedService } from 'src/app/services/bed.service';
import { BookingService } from 'src/app/services/booking.service';
import { JwtService } from 'src/app/services/jwt/jwt.service';
import { PatientService } from 'src/app/services/patient.service';

@Component({
  selector: 'app-bed-info',
  templateUrl: './bed-info.component.html',
  styleUrls: ['./bed-info.component.css'],
})
export class BedInfoComponent {
  @Input() selectedHospital: any;

  beds: Bed[] = [];
  userDetails!: UserDetails;
  patient!: Patient;
  addBookingRequest!: AddBookingRequest;

  regularBedACount: number = 0;
  regularBedBCount: number = 0;
  icuBedACount: number = 0;
  icuBedBCount: number = 0;
  ventilatorBedACount: number = 0;
  ventilatorBedBCount: number = 0;
  oxygenBedACount: number = 0;
  oxygenBedBCount: number = 0;
  regularBedCost: number = 0;
  icuBedCost: number = 0;
  ventilatorBedCost: number = 0;
  oxygenBedCost: number = 0;
  bedType: string = '';
  firstName: string = '';

  constructor(
    private bedService: BedService,
    private jwtService: JwtService,
    private patientService: PatientService,
    private ngToast: NgToastService,
    private bookingService: BookingService,
    private router: Router
  ) { }

  ngOnInit() {
    this.bedService
      .getBedsByHospitalId(this.selectedHospital.hospitalId)
      .subscribe(
        (response) => {
          this.beds = response;
          this.assignCount();
        },
        (error: HttpErrorResponse) => {
          this.handleError('Error', error.error);
        }
      );
  }

  //Populate variables for displaying bed details for booking purpose
  private assignCount() {
    for (const bed of this.beds) {
      if (bed.bedType === BedType.REGULAR_BED) {
        this.regularBedCost = bed.costPerDay;
        if (bed.bedStatus === 'AVAILABLE') {
          this.regularBedACount += 1;
        } else {
          this.regularBedBCount += 1;
        }
      } else if (bed.bedType === BedType.ICU_BED) {
        this.icuBedCost = bed.costPerDay;
        if (bed.bedStatus === 'AVAILABLE') {
          this.icuBedACount += 1;
        } else {
          this.icuBedBCount += 1;
        }
      } else if (bed.bedType === BedType.VENTILATOR_BED) {
        this.ventilatorBedCost = bed.costPerDay;
        if (bed.bedStatus === 'AVAILABLE') {
          this.ventilatorBedACount += 1;
        } else {
          this.ventilatorBedBCount += 1;
        }
      } else if (bed.bedType === BedType.OXYGEN_BED) {
        this.oxygenBedCost = bed.costPerDay;
        if (bed.bedStatus === 'AVAILABLE') {
          this.oxygenBedACount += 1;
        } else {
          this.oxygenBedBCount += 1;
        }
      }
    }
  }

  addBooking(fromDate: string, toDate: string) {
    //Create AddBook object
    const addBook: AddBookingRequest = {
      patientId: this.patient.patientId,
      hospitalId: this.selectedHospital.hospitalId,
      bedType: this.bedType,
      fromDate: fromDate,
      toDate: toDate,
    };

    console.log(addBook);

    // Call Add Booking API
    this.bookingService
      .addBooking(this.jwtService.getToken()!, addBook)
      .subscribe(
        (response) => {
          this.handleSuccess(
            'Booking has been registered!',
            'Please wait for the hospital to verify your booking request.'
          );
          this.router.navigate(['/userhome']);
        },
        (error: HttpErrorResponse) => {
          console.log(error);
          this.handleError('Error', error.error);
          this.router.navigate(['/userhome']);
        }
      );
  }

  // Load Patient details for displaying while booking
  loadPatient(bedtype: string) {
    this.bedType = bedtype;
    this.userDetails = this.jwtService.getUserDetails()!;

    this.patientService.findPatient(this.userDetails.referenceId).subscribe(
      (response) => {
        this.patient = response;
        this.firstName = this.patient.firstName;
      },
      (error: HttpErrorResponse) => {
        this.handleError(
          'Error in fetching patient details!',
          error.error
        );
      }
    );
  }

  //Handle Success messages
  private handleSuccess(msg: string, summary: string) {
    this.ngToast.success({
      detail: msg,
      summary: summary,
      duration: 5000,
    });
  }

  //Handle Error messages
  private handleError(msg: string, error: any) {
    this.ngToast.error({
      detail: msg,
      summary: error.error,
      duration: 5000,
    });
  }
}
