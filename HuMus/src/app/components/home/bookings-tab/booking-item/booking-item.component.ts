import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';
import { Booking } from 'src/app/models/booking.requests';
import { Hospital } from 'src/app/models/hospital.requests';
import { Patient } from 'src/app/models/patient.requests';
import { HospitalService } from 'src/app/services/hospital.service';
import { PatientService } from 'src/app/services/patient.service';
import { BookingStatus } from 'src/app/models/booking.requests';

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
  @Output() rejectClick = new EventEmitter<string>();
  @Output() approveClick = new EventEmitter<string>();
  @Output() cancelClick = new EventEmitter<string>();
  @Output() bookingSorted = new EventEmitter<Booking[]>();
  @Output() sortChange = new EventEmitter<{ column: string; order: string }>();

  BookingStatus = BookingStatus;
  patient: Patient;
  hospital: Hospital;
  isItemClicked: boolean = false;
  sortColumn: string = '';
  sortDirection: number = 1;
  selectedColumn: string;
  selectedOrder: string;

  constructor(
    private patientService: PatientService,
    private hospitalService: HospitalService,
    private toast: NgToastService
  ) {}

  onRejectClick(bookingId: string) {
    this.rejectClick.emit(bookingId);
  }

  onApproveClick(bookingId: string) {
    this.approveClick.emit(bookingId);
  }

  onCancelClick(bookingId: string) {
    this.cancelClick.emit(bookingId);
  }

  toggleItemClicked() {
    this.isItemClicked = !this.isItemClicked;
  }

  ngOnInit(): void {
    if (this.isManager) {
      this.fetchPatientDetails();
    }
    if (this.isPatient) {
      this.fetchHospitalDetails();
    }
  }
  
  private fetchPatientDetails() {
    this.patientService.findPatientById(this.booking.patientId).subscribe({
      next: (response: Patient) => {
        this.toast.info({
          detail: 'Success',
          summary: 'Patient details fetched',
          duration: 3000,
        });
        this.patient = response;
      },
      error: (error: HttpErrorResponse) => {
        this.toast.error({
          detail: 'Unable to fetch patient details',
          summary: error.error.toString(),
          duration: 3000,
        });
      },
    });
  }
  
  private fetchHospitalDetails() {
    this.hospitalService.getHospitalById(this.booking.hospitalId).subscribe({
      next: (response: Hospital) => {
        this.toast.info({
          detail: 'Success',
          summary: 'Hospital details fetched',
          duration: 3000,
        });
        this.hospital = response;
      },
      error: (error: HttpErrorResponse) => {
        this.toast.error({
          detail: 'Unable to fetch hospital details',
          summary: error.error.toString(),
          duration: 3000,
        });
      },
    });
  }  
}
