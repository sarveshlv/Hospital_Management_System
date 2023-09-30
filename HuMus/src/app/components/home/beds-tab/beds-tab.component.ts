import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';
import { AddBedRequest, Bed, BedStatus } from 'src/app/models/bed.requests';
import {
  AddBookingRequest,
  BedType,
  Booking,
} from 'src/app/models/booking.requests';
import { Hospital } from 'src/app/models/hospital.requests';
import { Patient } from 'src/app/models/patient.requests';
import { UserDetails } from 'src/app/models/user.requests';
import { BedService } from 'src/app/services/bed.service';
import { HospitalService } from 'src/app/services/hospital.service';
import { JwtStorageService } from 'src/app/services/jwt/jwt.storage.service';
import { PatientService } from 'src/app/services/patient.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BookingService } from 'src/app/services/booking.service';

@Component({
  selector: 'app-beds-tab',
  templateUrl: './beds-tab.component.html',
  styleUrls: ['./beds-tab.component.css'],
})
export class BedsTabComponent implements OnInit {
  isManager = false;
  isPatient = false;
  isAdmin = false;

  userDetails: UserDetails;
  patient: Patient;

  beds: Bed[] = [];
  filteredBeds: Bed[] = [];
  uniqueBeds: { bed: Bed; count: number }[] = [];

  hospitals: Hospital[] = [];
  selectedBedType: BedType | string = 'ALL';
  selectedBedStatus: BedStatus | string = 'ALL';
  selectedAddressFilter: string = 'PINCODE';
  selectedHospital: string = 'ALL';

  addBedPopup: boolean = false;
  bookBedPopup: boolean = false;
  addBedRequest: AddBedRequest = {} as AddBedRequest;
  addBookingRequest: AddBookingRequest = {} as AddBookingRequest;

  datePickerForm: FormGroup;

  constructor(
    private jwtStorageService: JwtStorageService,
    private bedsService: BedService,
    private patientService: PatientService,
    private hospitalService: HospitalService,
    private bookingService: BookingService,
    private toast: NgToastService,
    private fb: FormBuilder
  ) {
    this.datePickerForm = this.fb.group({
      occupyDate: ['', Validators.required],
      releaseDate: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.userDetails = this.jwtStorageService.getUserDetails();
    this.isManager = this.userDetails.role === 'MANAGER';
    this.isPatient = this.userDetails.role === 'USER';
    this.isAdmin = this.userDetails.role === 'ADMIN';

    this.addBedRequest.hospitalId = this.userDetails.referenceId;

    this.loadbeds();
    this.gethospitalList();
  }

  //loading beds based on profile
  private loadbeds(): void {
    if (this.isManager) {
      this.getBedsByHospitalId(this.userDetails.referenceId);
    } else if (this.isPatient) {
      this.getBedsByPincode();
    } else if (this.isAdmin) {
      this.getAllBeds();
    }
  }

  //methods for setting up filters
  get bedStausOptions() {
    return Object.values(BedStatus);
  }

  //methods for setting up filters
  get bedTypeOptions() {
    return Object.values(BedType);
  }

  //method call for setting filter by hospital name
  private gethospitalList(): void {
    this.hospitalService.getAllHospitals().subscribe({
      next: (response: Hospital[]) => {
        this.hospitals = response;
        this.toast.info({
          detail: 'Fetched hospital list',
          summary: `Fetched ${this.hospitals.length} hospitals for filtering`,
          duration: 3000,
        });
      },
      error: (error: HttpErrorResponse) => {
        this.toast.info({
          detail: 'Unable to fetch hospitals list',
          summary: error.error.error,
          duration: 3000,
        });
      },
    });
  }

  //method for filtering by bedType
  applyFilter() {
    if (this.selectedBedType === 'ALL' && this.selectedBedStatus === 'ALL') {
      this.filteredBeds = this.beds;
    } else {
      this.filteredBeds = this.beds.filter((bed) => {
        const bedTypeMatch =
          this.selectedBedType === 'ALL' ||
          bed.bedType === this.selectedBedType;
        const bedStatusMatch =
          this.selectedBedStatus === 'ALL' ||
          bed.bedStatus === this.selectedBedStatus;
        return bedStatusMatch && bedTypeMatch;
      });
      this.refreshUniqueBed();
    }
  }

  // filtering method for filtering by hospital
  applyHospitalFilter() {
    if (this.selectedHospital === 'ALL') {
      this.getAllBeds();
    } else {
      this.getBedsByHospitalId(this.selectedHospital);
    }
  }

  // filtering method for filtering by location - FOR Pateint only
  applyLocationFilter() {
    if (this.selectedAddressFilter === 'ALL') {
      this.getAllBeds();
    } else if (this.selectedAddressFilter === 'PINCODE') {
      this.getBedsByPincode();
    }
  }

  //method call for getting hospital by id - filtering and for loading MANAGER
  private getBedsByHospitalId(hospitalId: string): void {
    this.bedsService.getBedsByHospitalId(hospitalId).subscribe({
      next: (response: Bed[]) => {
        console.log('Fetched beds:', response);
        this.filteredBeds = response;
        this.toast.info({
          detail: 'Successfully fetched of hospital',
          summary: `Fetched ${this.filteredBeds.length} beds`,
          duration: 3000,
        });
      },
      error: (error: HttpErrorResponse) => {
        this.toast.error({
          detail: 'Unable to fetch beds of hospital',
          summary: error.error.error,
          duration: 3000,
        });
      },
      complete: () => this.refreshUniqueBed(),
    });
  }

  //method call for filteing by pincode - PATIENT
  private getBedsByPincode() {
    this.patientService
      .findPatientById(this.userDetails.referenceId)
      .subscribe({
        next: (response: Patient) => {
          this.patient = response;
          this.bedsService
            .getNearbyBeds(
              this.jwtStorageService.getToken(),
              this.patient.address.pincode
            )
            .subscribe({
              next: (response: Bed[]) => {
                this.beds = response;
                this.filteredBeds = this.beds;
                this.toast.info({
                  detail: 'Successfully fetched nearby beds',
                  summary: `Fetched ${this.filteredBeds.length} beds`,
                  duration: 3000,
                });
              },
              error: (error: HttpErrorResponse) => {
                this.toast.error({
                  detail: 'Unable to fetch beds nearby you',
                  summary: error.error.error,
                  duration: 3000,
                });
              },
              complete: () => this.refreshUniqueBed(),
            });
        },
        error: (error: HttpErrorResponse) => {
          this.toast.error({
            detail: 'Unable to fetch your location',
            summary: error.error.error,
            duration: 3000,
          });
        },
      });
  }

  // service calls method
  private getAllBeds(): void {
    this.bedsService.getAllBeds().subscribe({
      next: (response: Bed[]) => {
        this.beds = this.isPatient
          ? response.filter((bed) => bed.bedStatus === BedStatus.AVAILABLE)
          : response;
        this.filteredBeds = this.beds;
      },
      error: (error: HttpErrorResponse) => {
        this.toast.error({
          detail: 'Unable to fetch beds',
          summary: error.error.error,
          duration: 3000,
        });
      },
      complete: () => this.refreshUniqueBed(),
    });
  }

  //method to group similiar beds
  refreshUniqueBed(): void {
    this.uniqueBeds = [];
    this.filteredBeds.forEach((bed) => {
      const found = this.uniqueBeds.find((uniqueBed) =>
        this.areBedsEqual(bed, uniqueBed.bed)
      );
      if (found) {
        found.count++;
      } else {
        this.uniqueBeds.push({ bed, count: 1 });
      }
    });
    this.addBedPopup = this.refreshUniqueBed.length !== 0;
  }

  //utitilty beds to check beds to be similiar
  private areBedsEqual(bed1: Bed, bed2: Bed): boolean {
    return (
      bed1.hospitalId === bed2.hospitalId &&
      bed1.bedType === bed2.bedType &&
      bed1.costPerDay === bed2.costPerDay &&
      bed1.bedStatus === bed2.bedStatus
    );
  }

  //click actions
  closePopup() {
    this.addBedPopup = false;
    this.bookBedPopup = false;
  }

  //click action from bed item
  onAddBedClicked(uniqueBed: any) {
    this.addBedPopup = true;
    this.addBedRequest.bedType = uniqueBed.bed.bedType;
    this.addBedRequest.costPerDay = uniqueBed.bed.costPerDay;
  }

  //click action from bed item
  onBookBedClicked(uniqueBed: any) {
    this.bookBedPopup = true;
    this.addBookingRequest.hospitalId = uniqueBed.bed.hospitalId;
    this.addBookingRequest.bedType = uniqueBed.bed.bedType;
    this.addBookingRequest.patientId = this.userDetails.referenceId;
  }

  //handling click action to add bed
  addBed() {
    this.bedsService
      .addBed(this.jwtStorageService.getToken(), this.addBedRequest)
      .subscribe({
        next: (response: Bed) => {
          this.beds.push(response);
          this.toast.success({
            detail: 'Added bed successfully',
            summary:
              'Refresh the page or wait some time to fetch the bed into system',
            duration: 3000,
          });
          this.refreshUniqueBed();
        },
        error: (error: HttpErrorResponse) => {
          this.toast.error({
            detail: 'Unable to add bed. Plz try again later',
            summary: error.error.error,
            duration: 3000,
          });
        },
        complete: () => {
          this.addBedPopup = false;
          this.loadbeds();
        },
      });
  }

  //handling click action for booking bed
  bookBed() {
    if (this.datePickerForm.valid) {
      const occupyDateValue = this.datePickerForm.get('occupyDate').value;
      const releaseDateValue = this.datePickerForm.get('releaseDate').value;

      // Convert input values to date objects
      const bookingDate = new Date();
      const occupyDate = new Date(occupyDateValue);
      const releaseDate = new Date(releaseDateValue);
      if (bookingDate > occupyDate || occupyDate > releaseDate) {
        this.datePickerForm.reset();
        this.toast.error({
          detail: 'Dates must be after today',
          summary: 'Booking date must be before release date',
          duration: 3000,
        });
        return;
      } else {
        this.addBookingRequest.occupyDate = occupyDate;
        this.addBookingRequest.releaseDate = releaseDate;
        this.bookingService
          .addBooking(this.jwtStorageService.getToken(), this.addBookingRequest)
          .subscribe({
            next: (respone: Booking) =>
              this.toast.success({
                detail: 'Added booking request',
                summary:
                  'Waiting for approval. Your booking should be visible in bookings tab',
                duration: 3000,
              }),
            error: (error: HttpErrorResponse) => {
              console.log(error.error);
              this.toast.error({
                detail:
                  'Unable to add booking request. Please try again after some time',
                summary: error.error,
                duration: 3000,
              });
            },
            complete: () => (this.bookBedPopup = false),
          });
      }
    }
  }
}