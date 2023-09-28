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
  styleUrls: ['./booking-item.component.css']
})
export class BookingItemComponent implements OnInit{
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
  patient: Patient
  hospital: Hospital
  isItemClicked: boolean = false;
  sortColumn: string = '';
  sortDirection: number = 1;
  selectedColumn: string;
  selectedOrder: string;

  constructor(private patientService: PatientService, private hospitalService: HospitalService,  private toast: NgToastService){

  }

  onRejectClick(bookingId: string) {
    this.rejectClick.emit(bookingId);
  }

  onApproveClick(bookingId: string) {
    this.approveClick.emit(bookingId);
  }

  onCancelClick(bookingId: string) {
    this.cancelClick.emit(bookingId);
  }

  handleSort(column: string) {
    // Toggle sorting order if the same column is clicked again
    if (this.selectedColumn === column) {
      this.selectedOrder = this.selectedOrder === 'asc' ? 'desc' : 'asc';
    } else {
      // Default to ascending order when a new column is selected
      this.selectedColumn = column;
      this.selectedOrder = 'asc';
    }
  
    // Emit selected column and sorting order as an object
    this.sortChange.emit({ column: this.selectedColumn, order: this.selectedOrder });
  }

  
  toggleItemClicked() {
    this.isItemClicked = !this.isItemClicked;
  }
  
  ngOnInit(): void {
    console.log("BookingItemComponent: ngOnInit() -> ", "idx", this.idx, "Booking",this.booking );
    this.patientService.findPatientById(this.booking.patientId).subscribe({
      next: (response : Patient) => {
        console.log('Booking item: ngOnInit() -> ', 'Patient response', response);
        this.patient = response;
      },
      error: (error: HttpErrorResponse) => {
          this.handleError("Unable to fetch patient details", error.error);
      }
    })
    this.hospitalService.getHospitalById(this.booking.hospitalId).subscribe({
      next: (response: Hospital) =>{
        console.log('Booking item: ngOnInit() -> ', 'Hospital Response', response);
      },
      error: (error: HttpErrorResponse) => {
        this.handleError("Unable to fetch hospital details", error.error);
      }
    })
  }
  
  private handleError(errorMessage: string, error: any) {
    this.toast.error({
      detail: errorMessage,
      summary: error.error,
      duration: 3000,
    });
  }
}