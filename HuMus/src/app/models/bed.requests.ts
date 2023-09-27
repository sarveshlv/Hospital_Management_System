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