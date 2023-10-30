export class Bed {
  bedId: string;
  hospitalId: string;
  bedType: BedType;
  bedStatus: string;
  costPerDay: number;

  constructor(
    bedId: string,
    hospitalId: string,
    bedType: BedType,
    bedStatus: string,
    costPerDay: number
  ) {
    this.bedId = bedId;
    this.hospitalId = hospitalId;
    this.bedType = bedType;
    this.bedStatus = bedStatus;
    this.costPerDay = costPerDay;
  }
}

export enum BedType {
  REGULAR_BED = 'REGULAR_BED',
  ICU_BED = 'ICU_BED',
  OXYGEN_BED = 'OXYGEN_BED',
  VENTILATOR_BED = 'VENTILATOR_BED',
}

export enum BedStatus {
  AVAILABLE = 'AVAILABLE',
  BOOKED = 'BOOKED',
  CANCELLED = 'CANCELLED',
}

export interface AddBedRequest {
  hospitalId: string;
  bedType: string;
  costPerDay: number;
}
