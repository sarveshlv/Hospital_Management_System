export class Booking {
  bookingId: string;
  patientId: string;
  hospitalId: string;
  bedId: string;
  bedType: BedType;
  bookingDate: Date;
  fromDate: Date;
  toDate: Date;
  bookingStatus: BookingStatus;

  constructor(
    bookingId: string,
    patientId: string,
    hospitalId: string,
    bedId: string,
    bedType: BedType,
    bookingDate: Date,
    fromDate: Date,
    toDate: Date,
    bookingStatus: BookingStatus
  ) {
    this.bookingId = bookingId;
    this.patientId = patientId;
    this.hospitalId = hospitalId;
    this.bedId = bedId;
    this.bedType = bedType;
    this.bookingDate = bookingDate;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.bookingStatus = bookingStatus;
  }
}

export enum BookingStatus {
  REQUESTED = 'REQUESTED',
  APPROVED = 'APPROVED',
  DECLINED = 'DECLINED',
  CANCELLED = 'CANCELLED',
  COMPLETED = 'COMPLETED',
}

export enum BedType {
  REGULAR_BED = 'REGULAR_BED',
  ICU_BED = 'ICU_BED',
  OXYGEN_BED = 'OXYGEN_BED',
  VENTILATOR_BED = 'VENTILATOR_BED',
}
export interface AddBookingRequest {
  patientId: string;
  hospitalId: string;
  bedType: string;
  fromDate: string;
  toDate: string;
}
