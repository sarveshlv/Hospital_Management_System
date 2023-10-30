import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import { AddBedRequest, Bed } from 'src/app/models/bed.requests';
import { Booking, BookingStatus } from 'src/app/models/booking.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { BedService } from 'src/app/services/bed.service';
import { BookingService } from 'src/app/services/booking.service';
import { HospitalService } from 'src/app/services/hospital.service';
import { JwtService } from 'src/app/services/jwt/jwt.service';
import { PatientService } from 'src/app/services/patient.service';

@Component({
  selector: 'app-managerhome',
  templateUrl: './managerhome.component.html',
  styleUrls: ['./managerhome.component.css'],
})
export class ManagerhomeComponent {
  userDetails!: UserDetails;
  bookings: Booking[] = [];
  bookingsByStatus: Booking[] = [];
  bedTypes: string[] = [];
  beds: Bed[] = [];
  patientNames: { [key: string]: string } = {};

  showBookings = true;
  showBeds = false;
  showHistory = false;
  loggedInUserName: string = '';
  selectedBedType: string = '';
  costPerDay: number = 0;
  searchOption: string = '';
  bedId: string = '';
  bedType: string = '';
  cost: number = 0;
  searchType: string = 'Select Bed type';
  availability: string = 'Select status';
  showForm: boolean = false;

  constructor(
    private jwtService: JwtService,
    private bookingService: BookingService,
    private patientService: PatientService,
    private ngToast: NgToastService,
    private bedService: BedService,
    private hospitalService: HospitalService
  ) {}

  //Load specific bookings based on status on component load
  ngOnInit() {
    this.userDetails = this.jwtService.getUserDetails()!;
    this.loggedInUserName = this.userDetails.firstName;

    this.bookingService
      .getBookingByHospitalId(this.userDetails.referenceId)
      .subscribe(
        (response) => {
          this.bookings = response;
          this.bookingsByStatus = this.bookings.filter((booking) => {
            return booking.bookingStatus === BookingStatus.REQUESTED;
          });

          //Map patient IDs to Patient names
          for(const booking of this.bookings) {
            this.patientService.findPatient(booking.patientId).subscribe(
              (response) => {
                this.patientNames[booking.patientId] = response.firstName;
                // console.log(this.patientNames);
              }
            )
        }
        },
        (error: HttpErrorResponse) => {
          this.handleError('Error', error.error);
        }
      );
  }

  //Function to get the Patient Names of each booking
  getPatientName(patientId: string): string {
    return this.patientNames[patientId];
  }

  //Function to call approveBooking service on click
  approveBooking(booking: Booking) {
    this.bookingService.approveBooking(booking.bookingId).subscribe(
      (response) => {
        this.handleSuccess('Booking Approved!', '');
        this.ngOnInit();
      },
      (error:HttpErrorResponse) => {
        // console.log(error);
        this.handleError('Error',error.error);
      }
    );
  }

  //Fucntion to call declineBooking service on click
  declineBooking(booking: Booking) {
    this.bookingService
      .declineBooking(this.jwtService.getToken()!, booking.bookingId)
      .subscribe(
        (response) => {
          // console.log(response);
          this.ngToast.info({
            detail: 'Booking Declined!',
            summary: '',
            duration: 5000
          })
          this.ngOnInit();
        },
        (error:HttpErrorResponse) => {
          // console.log(error);
          this.handleError('Error',error.error);
        }
      );
  }

  //Function to load beds in real time after performing actions
  loadBeds() {
    this.bedService
      .getBedsByHospitalId(this.userDetails.referenceId)
      .subscribe((response) => {
        // console.log(response);
        this.beds = response;
      });
  }

  //Function to call AddBed service and add a new bed into the database
  addBed() {
    const bed: AddBedRequest = {
      hospitalId: this.userDetails.referenceId,
      bedType: this.selectedBedType,
      costPerDay: this.costPerDay,
    };

    this.bedService.registerBed(this.jwtService.getToken()!, bed).subscribe(
      (response) => {
        // console.log(response);
        this.handleSuccess('Bed added successfully!', '');
      },
      (error:HttpErrorResponse) => {
        // console.log(error);
        this.handleError('Error', error.error);
      }
    );
  }

  //Function for processing search and filter fucntions
  applyFilter() {
    if (this.searchOption === 'BedType') {
      this.beds = this.beds.filter((bed) => {
        return bed.bedType === this.searchType;
      });
      // console.log(this.beds);
    } else {
      this.beds = this.beds.filter((bed) => {
        return bed.bedStatus === this.availability;
      });
    }
  }

  
  //Function to populate the editModel
  editInfo(bed: Bed) {
    this.bedId = bed.bedId;
    this.bedType = bed.bedType;
    this.cost = bed.costPerDay;
  }

  //Function to call updateBed service
  updateBed() {
    this.bedService
      .updateBedDetails(this.bedId, this.cost)
      .subscribe((response) => {
        // console.log(response);
        this.handleSuccess('Bed details updated successfully!', '');
      }, 
      (error: HttpErrorResponse) => {
        this.handleError('Error', error.error);
      });
  }

  //Function to call deleteBed service 
  deleteBed(bed: Bed) {
    this.bedService.deleteBed(bed.bedId).subscribe((response) => {
      // console.log(response);
      this.handleSuccess('Bed deleted!', '');
      this.loadBeds();
    }, (error:HttpErrorResponse) => {
      // console.log(error);
      this.handleError('Cannot delete Bed!', error.error);
    });
  }

  //Fucntion to check whether the hospital is verified or not before adding a bed
  isVerifiedHospital() {
    this.hospitalService
      .getHospitalById(this.userDetails.referenceId)
      .subscribe((response) => {
        if (response.status === 'REQUESTED') {
          this.showForm = false;
          this.ngToast.info({
            detail: 'Hospital not approved yet!',
            summary:
              'You can add beds once the manager verifies your hospital details.',
            duration: 8000,
          });
        } else {
          this.showForm = true;
        }
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

  //Handle success messages
  private handleSuccess(msg: string, summary: string) {
    this.ngToast.success({
      detail: msg,
      summary: summary,
      duration: 5000,
    });
  }
}
