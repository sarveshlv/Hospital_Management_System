export class Bed {
  id: string;
  hospitalId: string;
  bedType: BedType;
  bedStatus: BedStatus;
  costPerDay: number;

  constructor(
    id: string,
    hospitalId: string,
    bedType: BedType,
    bedStatus: BedStatus,
    costPerDay: number
  ) {
    this.id = id;
    this.hospitalId = hospitalId;
    this.bedType = bedType;
    this.bedStatus = bedStatus;
    this.costPerDay = costPerDay;
  }

  isEqual(otherBed: Bed): boolean {
    return (
      this.hospitalId === otherBed.hospitalId &&
      this.bedType === otherBed.bedType &&
      this.bedStatus === otherBed.bedStatus &&
      this.costPerDay === otherBed.costPerDay
    );
  }
}

export enum BedType {
  USUAL_BED = 'USUAL_BED',
  ICU_BED = 'ICU_BED',
  OXYGEN_BED = 'OXYGEN_BED',
  VENTILATOR_BED = 'VENTILATOR_BED',
}

export enum BedStatus {
  AVAILABLE = 'AVAILABLE',
  BOOKED = 'BOOKED',
  CANCELLED = 'CANCELLED',
  COMPLETED = 'COMPLETED',
}

export interface AddBedRequest {
  hospitalId: string;
  bedType: BedType;
  costPerDay: number;
}

export interface UpdateBedRequest {
  bedType: BedType;
  costPerDay: number;
}