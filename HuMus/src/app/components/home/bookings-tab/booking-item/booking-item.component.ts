import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';
import { Booking } from 'src/app/models/booking.requests';
import { Hospital } from 'src/app/models/hospital.requests';
import { Patient } from 'src/app/models/patient.requests';
import { HospitalService } from 'src/app/services/hospital.service';
import { PatientService } from 'src/app/services/patient.service';
import { BookingStatus } from 'src/app/models/booking.requests';
import { BillingService } from 'src/app/services/billing.service';
import { Billing, PaymentStatus } from 'src/app/models/billing.requests';
import { Router } from '@angular/router';

@Component({
  selector: 'app-booking-item',
  templateUrl: './booking-item.component.html',
  styleUrls: ['./booking-item.component.css'],
})
export class BookingItemComponent implements OnInit {
  @Input() idx: number;
  @Input() booking: Booking;
  @Input() isManager: boolean;
  @Input() isPatient: boolean;
  @Input() isAdmin: boolean;
  @Output() rejectClick = new EventEmitter<string>();
  @Output() approveClick = new EventEmitter<string>();
  @Output() cancelClick = new EventEmitter<string>();
  @Output() completeClick = new EventEmitter<Booking>();

  BookingStatus = BookingStatus;
  PaymentStaus = PaymentStatus;
  patient: Patient;
  hospital: Hospital;
  billing: Billing;
  selectedColumn: string;
  selectedOrder: string;

  constructor(
    private patientService: PatientService,
    private hospitalService: HospitalService,
    private billingService: BillingService,
    private toast: NgToastService
  ) {}

  ngOnInit(): void {
    if (this.booking.bookingStatus === BookingStatus.COMPLETED) {
      this.billingService.findByBookingId(this.booking.id).subscribe({
        next: (response: Billing) => {
          console.log(response);
          this.billing = response;
        },
        error: (error: HttpErrorResponse) =>
          this.toast.error({
            detail: 'Unable to find billing details',
            summary: error.error.error,
            duration: 3000,
          }),
      });
    }

    if (this.isManager || this.isAdmin)
      this.fetchPatientDetails(this.booking.patientId);

    if (this.isPatient || this.isAdmin)
      this.fetchHospitalDetails(this.booking.hospitalId);

    if (
      this.booking.bookingStatus === BookingStatus.REQUESTED &&
      this.booking.occupyDate < new Date()
    ) {
      //auto reject booking as booking date has already passed
      this.onRejectClick(this.hospital.id);
    }

    if (
      this.booking.bookingStatus === BookingStatus.APPROVED &&
      this.booking.releaseDate >= new Date()
    ) {
      //booking should be marked completed as, today is the release date
      this.onCompleteClick(this.booking);
    }
  }

  //fetch methods
  private fetchPatientDetails(patientId: string) {
    this.patientService.findPatientById(patientId).subscribe({
      next: (response: Patient) => (this.patient = response),
      error: (error: HttpErrorResponse) => {
        this.toast.error({
          detail: 'Unable to fetch patient details',
          summary: error.error.toString(),
          duration: 3000,
        });
      },
    });
  }

  private fetchHospitalDetails(hospitalId: string) {
    this.hospitalService.getHospitalById(hospitalId).subscribe({
      next: (response: Hospital) => (this.hospital = response),
      error: (error: HttpErrorResponse) => {
        this.toast.error({
          detail: 'Unable to fetch hospital details',
          summary: error.error.toString(),
          duration: 3000,
        });
      },
    });
  }

  //click actions
  onRejectClick(bookingId: string) {
    this.rejectClick.emit(bookingId);
  }

  onApproveClick(bookingId: string) {
    this.approveClick.emit(bookingId);
  }

  onCancelClick(bookingId: string) {
    this.cancelClick.emit(bookingId);
  }
  onCompleteClick(booking: Booking) {
    if (
      confirm(
        'Are you sure to complete booking. If this is a mistake, ur bill will be generated for days ur booking was for and you will be charged accordingly according to the policy'
      )
    ) {
      this.completeClick.emit(booking);
    }
  }
  payBill() {
    this.billingService.initiatePayment(this.billing.id).subscribe({
      next: (response: any) => {
        this.toast.error({
          detail: 'Unable to initiate payment',
          summary: 'Plz try again later',
          duration: 3000,
        });
      },
      error: (error: HttpErrorResponse) => {
        console.log(error.error.text);
        window.open(error.error.text, '_blank');
      },
    });
  }

  //conditionals for showing td's
  shouldDisplayApproval(booking: Booking): boolean {
    return this.isManager && booking.bookingStatus === BookingStatus.REQUESTED;
  }

  shouldDisplayPatientActions(booking: Booking): boolean {
    return (
      this.isPatient &&
      (booking.bookingStatus === BookingStatus.REQUESTED ||
        booking.bookingStatus === BookingStatus.APPROVED ||
        booking.bookingStatus === BookingStatus.COMPLETED)
    );
  }

  shouldDisplayPaymentButton(booking: Booking): boolean {
    return (
      this.isPatient &&
      booking.bookingStatus === BookingStatus.COMPLETED &&
      this.billing.paymentStatus === PaymentStatus.PENDING
    );
  }

  shouldDisplayNoAction(booking: Booking): boolean {
    return (
      !this.isAdmin &&
      !(
        this.shouldDisplayApproval(booking) ||
        this.shouldDisplayPatientActions(booking) ||
        this.shouldDisplayPaymentButton(booking)
      )
    );
  }
}
