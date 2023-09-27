export class Booking {
  id: string;
  patientId: string;
  hospitalId: string;
  bedId: string;
  bedType: BedType;
  bookingDate: Date;
  occupyDate: Date;
  releaseDate: Date;
  bookingStatus: BookingStatus;

  constructor(
    id: string,
    patientId: string,
    hospitalId: string,
    bedId: string,
    bedType: BedType,
    bookingDate: Date,
    occupyDate: Date,
    releaseDate: Date,
    bookingStatus: BookingStatus
  ) {
    this.id = id;
    this.patientId = patientId;
    this.hospitalId = hospitalId;
    this.bedId = bedId;
    this.bedType = bedType;
    this.bookingDate = bookingDate;
    this.occupyDate = occupyDate;
    this.releaseDate = releaseDate;
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
  USUAL_BED = 'USUAL_BED',
  ICU_BED = 'ICU_BED',
  OXYGEN_BED = 'OXYGEN_BED',
  VENTILATOR_BED = 'VENTILATOR_BED',
}
export interface AddBookingRequest {
  patientId: string;
  hospitalId: string;
  bedType: BedType;
  occupyDate: Date;
  releaseDate: Date;
}