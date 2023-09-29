import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgToastService } from 'ng-angular-popup';
import { Bed, BedStatus } from 'src/app/models/bed.requests';
import { Hospital } from 'src/app/models/hospital.requests';
import { HospitalService } from 'src/app/services/hospital.service';

@Component({
  selector: 'app-bed-item-component',
  templateUrl: './bed-item-component.component.html',
  styleUrls: ['./bed-item-component.component.css'],
})
export class BedItemComponentComponent implements OnInit {
  @Input() uniqueBed: any;
  @Input() isManager: boolean;
  @Input() isPatient: boolean; 
  @Output() addBedClicked: EventEmitter<any> = new EventEmitter<any>();
  @Output() bookBedClicked: EventEmitter<void> = new EventEmitter<any>();


  hospital: Hospital;
  BedStaus = BedStatus;
  constructor(
    private hospitalService: HospitalService,
    private toast: NgToastService,

  ) {}

  ngOnInit(): void {
    this.hospitalService.getHospitalById(this.uniqueBed.bed.hospitalId).subscribe({
      next: (response: Hospital) => {
        this.hospital = response;
      },
      error: (error: HttpErrorResponse) => {
        this.toast.error({
          detail:`Unable to fetch hospital details for bed ${this.uniqueBed.bed.id}`,
          summary: error.error.toString(),
          duration: 3000
        });
      },
    });
  }
  
  bookBed(uniqueBed: any){
    this.bookBedClicked.emit(uniqueBed);
  }
  addBed(uniqueBed: any){
    this.addBedClicked.emit(uniqueBed);
  }
}
