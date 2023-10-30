import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';
// import * as Razorpay from 'razorpay';
import { Billing, PaymentDetails } from 'src/app/models/billing.requests';
import { Booking, BookingStatus } from 'src/app/models/booking.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { BillingService } from 'src/app/services/billing.service';
import { BookingService } from 'src/app/services/booking.service';
import { HospitalService } from 'src/app/services/hospital.service';
import { JwtService } from 'src/app/services/jwt/jwt.service';

declare var Razorpay: any;

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css'],
})
export class HistoryComponent {
  userDetails!: UserDetails;
  bookingDetails: Booking[] = [];
  billId: string = '';
  bookingId: string = '';
  billAmount: number = 0;
  paymentStatus: string = '';
  hName: string = '';

  constructor(
    private jwtService: JwtService,
    private ngToast: NgToastService,
    private bookingService: BookingService,
    private billingService: BillingService,
    private hospitalService: HospitalService
  ) { }

  //View specific bookings based on status on component load
  ngOnInit() {
    this.userDetails = this.jwtService.getUserDetails()!;

    this.bookingService
      .getBookingsByPatientId(this.userDetails.referenceId)
      .subscribe(
        (response) => {
          this.bookingDetails = response;
          this.bookingDetails = this.bookingDetails.filter((booking) => {
            return (
              booking.bookingStatus === BookingStatus.CANCELLED ||
              booking.bookingStatus === BookingStatus.COMPLETED ||
              booking.bookingStatus === BookingStatus.DECLINED
            );
          });
        },
        (error: HttpErrorResponse) => {
          this.handleError("Couldn't fetch booking details", error.error);
        }
      );
  }

  //Function to get the billing details of a particular booking
  viewBill(booking: Booking) {
    this.billingService.findByBookingId(booking.bookingId).subscribe(
      (response) => {

        this.hospitalService.getHospitalById(booking.hospitalId).subscribe(
          (response) => {
            this.hName = response.hospitalName;
            // console.log(this.hName);
          },
          (error: HttpErrorResponse) => {
            this.handleError('Error', error.error);
          })
        this.billId = response.billId;
        this.bookingId = response.bookingId;
        this.billAmount = response.billAmount;
        this.paymentStatus = response.paymentStatus;
      },
      (error: HttpErrorResponse) => {
        this.handleError(
          'Error in fetching booking details!',
          error.error
        );
      }
    );
  }

  //Function to call the payment service
  // payBill() {
  //   this.billingService.initiatePayment(this.billId).subscribe({
  //     next: (response: any) => {
  //       this.ngToast.error({
  //         detail: 'Unable to initiate payment',
  //         summary: 'Plz try again later',
  //         duration: 5000,
  //       });
  //     },
  //     error: (error: HttpErrorResponse) => {
  //       // console.log(error.error.text);
  //       window.open(error.error.text, '_blank');
  //     },
  //   });
  // }

  payBill() {
    this.billingService.initiatePayment(this.billId).subscribe({
      next: (response: PaymentDetails) => {
        // console.log("Payment Details ", response);
        var options = {
          order_id: response.id,
          key_id: response.keyId,
          amount: response.amount,
          currency: response.currency,
          name: `${this.userDetails.firstName} ${this.userDetails.lastName}`,
          description: 'Bill for booking',
          // image:,
          handler: (response: any) => {
            if (response != null && response.razorpay_payment_id != null) {
              // console.log("Success");
              this.processPaymentSuccess(this.billId);
            } else {
              this.ngToast.error({
                detail: 'Payment failed',
                summary: 'Plz try again later',
                duration: 3000,
              });
            }
          },
          prefill: {
            name: this.userDetails.firstName + " " + this.userDetails.lastName,
            email: this.userDetails.emailId,
          },
          themes: {
            color: '#F3754'
          }

        };
        // console.log("Options", options);
        this.ngToast.info({
          detail: 'Redirecting to payment page',
          summary: 'Please complete payment',
          duration: 3000,
        });

        var razorPayObject = new Razorpay(options);
        razorPayObject.open();
      },
      error: (error: HttpErrorResponse) => {
        // console.log(error.error.text);
        this.ngToast.error({
          detail: 'Unable to start payment',
          summary: error.error.error,
          duration: 3000,
        });
      },
    });
  }

  processPaymentSuccess(billingId: string) {
    this.billingService.successPay(billingId).subscribe({
      next: (response: Billing) => {
        this.ngToast.success({
          detail: 'Payment Succesfull',
          summary: 'Status is being upddated in out system',
          duration: 3000,
        });
      },
      error: (error: HttpErrorResponse) => {
        this.ngToast.error({
          detail: 'Unable to update payment status',
          summary: error.error.error,
          duration: 3000,
        });
      },
    });
    throw new Error('Method not implemented.');
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
