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
  @Output() bookingSorted = new EventEmitter<Booking[]>();

  BookingStatus = BookingStatus;
  patient: Patient;
  hospital: Hospital;
  selectedColumn: string;
  selectedOrder: string;

  constructor(
    private patientService: PatientService,
    private hospitalService: HospitalService,
    private billingService: BillingService,
    private toast: NgToastService
  ) {}

  ngOnInit(): void {
    if (this.isManager || this.isAdmin)
      this.fetchPatientDetails(this.booking.patientId);

    if (this.isPatient || this.isAdmin)
      this.fetchHospitalDetails(this.booking.hospitalId);

    if(this.booking.bookingStatus === BookingStatus.REQUESTED && this.booking.occupyDate < new Date()){
      //auto reject booking as booking date has already passed
      this.onRejectClick(this.hospital.id);
    }

    if(this.booking.bookingStatus === BookingStatus.APPROVED && this.booking.releaseDate >= new Date()){
      //booking should be marked completed as, today is the release date
      // this.billingService.addBilling()
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
}