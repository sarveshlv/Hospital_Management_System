import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';
import { BedType } from 'src/app/models/bed.requests';
import { Booking, BookingStatus } from 'src/app/models/booking.requests';
import { UserDetails } from 'src/app/models/user.requests';
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
  userDetails: UserDetails;
  idx = -1;
  selectedFilter: BookingStatus | string = 'ALL';
  selectedBedType: BedType | string = 'ALL';

  bookings: Booking[] = [];
  filteredBookings: Booking[] = [];

  constructor(
    private jwtStorageService: JwtStorageService,
    private bookingsService: BookingService,
    private toast: NgToastService
  ) {}

  ngOnInit(): void {
    this.userDetails = this.jwtStorageService.getUserDetails();
    this.isManager = this.userDetails.role === 'MANAGER';
    this.isPatient = this.userDetails.role === 'USER';

    this.loadBookings();
  }

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
    }

    if (bookingsObservable) {
      bookingsObservable.subscribe({
        next: (response: Booking[]) => {
          console.log('Fetched bookings:', response);
          this.bookings = response;
          this.applyFilter();
        },
        error: (error: HttpErrorResponse) => {
          this.handleError(
            `Unable to fetch ${
              this.isManager ? 'hospital' : 'patient'
            } bookings`,
            error.error
          );
          console.log(error.error.error);
        },
      });
    }
  }

  get bookingStatusOptions() {
    return Object.values(BookingStatus);
  }

  get bedTypeOptions() {
    return Object.values(BedType);
  }

  private updateBooking(response: Booking, action: string): void {
    console.log(
      `Booking tab: handle${
        action.charAt(0).toUpperCase() + action.slice(1)
      }Click() -> ${action}ed Booking`,
      response
    );
    const index = this.bookings.findIndex(
      (booking) => booking.id === response.id
    );
    if (index !== -1) {
      this.bookings[index] = response;
      this.applyFilter();
    }
  }

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

  handleSortChange(sortObj: { column: string; order: string }) {
    // Handle the selected column and sorting order together
    const { column, order } = sortObj;
    // You can update the sorting logic based on the selected column and order here
    this.filteredBookings.sort((a: any, b: any) => {
      let comparison = 0;
  
      if (order === 'asc') {
        if (a[column] > b[column]) {
          comparison = 1;
        } else if (a[column] < b[column]) {
          comparison = -1;
        }
      } else if (order === 'desc') {
        if (a[column] < b[column]) {
          comparison = 1;
        } else if (a[column] > b[column]) {
          comparison = -1;
        }
      }
  
      return comparison;
    });
  
    // Update the filteredBookings array with the sorted results
    this.filteredBookings = [...this.filteredBookings];
  }

  handleApproveClick(bookingId: string): void {
    this.bookingsService
      .approveBooking(this.jwtStorageService.getToken(), bookingId)
      .subscribe({
        next: (response: Booking) => this.updateBooking(response, 'approve'),
        error: (error: HttpErrorResponse) =>
          this.handleError('Unable to approve booking', error.error),
      });
  }

  handleCancelClick(bookingId: string): void {
    this.bookingsService
      .cancelBooking(this.jwtStorageService.getToken(), bookingId)
      .subscribe({
        next: (response: Booking) => this.updateBooking(response, 'cancel'),
        error: (error: HttpErrorResponse) =>
          this.handleError('Unable to cancel booking', error.error),
      });
  }

  handleRejectClick(bookingId: string): void {
    this.bookingsService.rejectBooking(bookingId).subscribe({
      next: (response: Booking) => this.updateBooking(response, 'reject'),
      error: (error: HttpErrorResponse) =>
        this.handleError('Unable to reject booking', error.error),
    });
  }

  private handleError(errorMessage: string, error: any) {
    this.toast.error({
      detail: errorMessage,
      summary: error.error,
      duration: 3000,
    });
  }
}