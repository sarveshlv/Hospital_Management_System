import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';
import { BedType } from 'src/app/models/bed.requests';
import { Billing, BillingRequest } from 'src/app/models/billing.requests';
import { Booking, BookingStatus } from 'src/app/models/booking.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { BillingService } from 'src/app/services/billing.service';
import { BookingService } from 'src/app/services/booking.service';
import { JwtStorageService } from 'src/app/services/jwt/jwt.storage.service';

@Component({
  selector: 'app-bookings-tab',
  templateUrl: './bookings-tab.component.html',
  styleUrls: ['./bookings-tab.component.css'],
})
export class BookingsTabComponent implements OnInit {
  isManager = false;
  isPatient = false;
  isAdmin = false;

  userDetails: UserDetails;

  idx = -1;
  bookings: Booking[] = [];
  filteredBookings: Booking[] = [];

  selectedFilter: BookingStatus | string = 'ALL';
  selectedBedType: BedType | string = 'ALL';

  constructor(
    private jwtStorageService: JwtStorageService,
    private bookingsService: BookingService,
    private billingService: BillingService,
    private toast: NgToastService
  ) {}

  ngOnInit(): void {
    this.userDetails = this.jwtStorageService.getUserDetails();
    this.isManager = this.userDetails.role === 'MANAGER';
    this.isPatient = this.userDetails.role === 'USER';
    this.isAdmin = this.userDetails.role === 'ADMIN';

    this.loadBookings();
  }

  //loading bookings based on profiles
  private loadBookings(): void {
    let bookingsObservable;

    if (this.isManager) {
      bookingsObservable = this.bookingsService.getBookingByHospitalId(
        this.userDetails.referenceId
      );
    } else if (this.isPatient) {
      bookingsObservable = this.bookingsService.getBookingsByPatientId(
        this.userDetails.referenceId
      );
    } else if (this.isAdmin) {
      bookingsObservable = this.bookingsService.findAll();
    }

    if (bookingsObservable) {
      bookingsObservable.subscribe({
        next: (response: Booking[]) => {
          this.toast.info({
            detail: 'Bookings fetched',
            summary: `Retrieved ${response.length} bookings`,
            duration: 3000,
          });
          this.bookings = response;
          this.applyFilter();
        },
        error: (error: HttpErrorResponse) => {
          this.toast.error({
            detail: `Unable to fetch ${
              this.isManager ? 'hospital' : 'patient'
            } bookings`,
            summary: error.error.error,
            duration: 3000,
          });
        },
      });
    }
  }

  //setting up filters
  get bookingStatusOptions() {
    return Object.values(BookingStatus);
  }

  //seting up filters
  get bedTypeOptions() {
    return Object.values(BedType);
  }

  private updateBooking(response: Booking): void {
    this.toast.success({
      detail: 'Success',
      summary: 'Booking updated',
      duration: 3000,
    });
    const index = this.bookings.findIndex(
      (booking) => booking.id === response.id
    );
    if (index !== -1) {
      this.bookings[index] = response;
      this.applyFilter();
    }
  }

  //method to apply filters
  applyFilter() {
    if (this.selectedFilter === 'ALL' && this.selectedBedType === 'ALL') {
      this.filteredBookings = this.bookings;
    } else {
      this.filteredBookings = this.bookings.filter((booking) => {
        const statusMatch =
          this.selectedFilter === 'ALL' ||
          booking.bookingStatus === this.selectedFilter;
        const bedTypeMatch =
          this.selectedBedType === 'ALL' ||
          booking.bedType === this.selectedBedType;
        return statusMatch && bedTypeMatch;
      });
    }
  }

  //methods for click events
  handleApproveClick(bookingId: string): void {
    this.bookingsService
      .approveBooking(this.jwtStorageService.getToken(), bookingId)
      .subscribe({
        next: (response: Booking) => this.updateBooking(response),
        error: (error: HttpErrorResponse) =>
          this.toast.error({
            detail: 'Unable to approve booking',
            summary: error.error.error,
            duration: 3000,
          }),
      });
  }

  //methods for click events
  handleCancelClick(bookingId: string): void {
    this.bookingsService
      .cancelBooking(this.jwtStorageService.getToken(), bookingId)
      .subscribe({
        next: (response: Booking) => this.updateBooking(response),
        error: (error: HttpErrorResponse) =>
          this.toast.error({
            detail: 'Unable to cancel booking',
            summary: error.error.error,
            duration: 3000,
          }),
      });
  }

  //methods for click events
  handleRejectClick(bookingId: string): void {
    this.bookingsService.rejectBooking(bookingId).subscribe({
      next: (response: Booking) => this.updateBooking(response),
      error: (error: HttpErrorResponse) =>
        this.toast.error({
          detail: 'Unable to reject booking',
          summary: error.error.error,
          duration: 3000,
        }),
    });
  }
  
  //methods for click events
  handleCompleteClick(booking: Booking): void {
    const billingRequest: BillingRequest = {
      bookingId: booking.id
    }

    this.billingService.addBilling(this.jwtStorageService.getToken(), billingRequest).subscribe({
      next: (response: Billing) => {
        booking.bookingStatus = BookingStatus.COMPLETED;
        this.updateBooking(booking);
      },
      error: (error: HttpErrorResponse) =>{
        console.log(error);
        this.toast.error({
          detail: 'Unable to complete booking',
          summary: error.error.error,
          duration: 3000,
        });
      }
    });
  }
}
