import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';
import { BillingRequest } from 'src/app/models/billing.requests';
import { Booking, BookingStatus } from 'src/app/models/booking.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { BillingService } from 'src/app/services/billing.service';
import { BookingService } from 'src/app/services/booking.service';
import { HospitalService } from 'src/app/services/hospital.service';
import { JwtService } from 'src/app/services/jwt/jwt.service';

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.css'],
})
export class StatusComponent {
  userDetails!: UserDetails;
  bookingDetails: Booking[] = [];
  hName: string = 'View name';

  constructor(
    private jwtService: JwtService,
    private bookingService: BookingService,
    private ngToast: NgToastService,
    private billingService: BillingService,
    private hospitalService: HospitalService
  ) {}

  ngOnInit() {
    this.userDetails = this.jwtService.getUserDetails()!;

    this.bookingService
      .getBookingsByPatientId(this.userDetails.referenceId)
      .subscribe(
        (response) => {
          this.bookingDetails = response;
          this.bookingDetails = this.bookingDetails.filter((booking) => {
            return (
              booking.bookingStatus === BookingStatus.REQUESTED ||
              booking.bookingStatus === BookingStatus.APPROVED
            );
          });
        },
        (error: HttpErrorResponse) => {
          this.handleError("Couldn't fetch booking details", error.error);
        }
      );
  }

  //Function to get hospitalName for displaying in the row
  findName(hospitalId: string): string {
    this.hospitalService.getHospitalById(hospitalId).subscribe(
      (response) => {
        this.hName = response.hospitalName;
        // console.log(this.hName);
      },
      (error: HttpErrorResponse) => {
        this.handleError('Error', error.error);
      }
    );
    return this.hName;
  }

  //Fucntion to call billing service to generate bill on booking complete
  completeBooking(booking: Booking) {
    const billingRequest: BillingRequest = {
      bookingId: booking.bookingId,
    };

    this.billingService
      .addBilling(this.jwtService.getToken()!, billingRequest)
      .subscribe(
        (response) => {
          this.handleSuccess('Your Bill has been generated!', '');
          // console.log(response);
          this.ngOnInit();
        },
        (error: HttpErrorResponse) => {
          this.handleError('Error', error.error);
        }
      );
  }

  //Function to call cancelBooking service to cancel to booking
  cancelBooking(booking: Booking) {
    this.bookingService.cancelBooking(this.jwtService.getToken()!, booking.bookingId).subscribe(
      (response) => {
        this.handleSuccess('Booking Cancelled!', '');
        this.ngOnInit();
      }, 
      (error:HttpErrorResponse) => {
        this.handleError('Error', error.error);
      }
    )
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
