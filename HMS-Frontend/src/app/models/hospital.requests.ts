export class Hospital {
  hospitalId: string;
  hospitalName: string;
  hospitalAddress: Address;
  pincode: number;
  hospitalType: HospitalType;
  status: string;
  contactNo: number;

  constructor(
    hospitalId: string,
    hospitalName: string,
    hospitalAddress: Address,
    pincode: number,
    hospitalType: HospitalType,
    status: string,
    contactNo: number
  ) {
    this.hospitalId = hospitalId;
    this.hospitalName = hospitalName;
    this.hospitalAddress = hospitalAddress;
    this.pincode = pincode;
    this.hospitalType = hospitalType;
    this.status = status;
    this.contactNo = contactNo;
  }
}

export class Address {
  streetAddress: string;
  cityName: string;
  stateName: string;

  constructor(streetAddress: string, city: string, state: string) {
    this.streetAddress = streetAddress;
    this.cityName = city;
    this.stateName = state;
  }
}

export enum HospitalType {
  GOVERNMENT = 'GOVERNMENT',
  PRIVATE = 'PRIVATE',
}

export interface AddHospitalRequest {
  hospitalName: string;
  hospitalType: HospitalType;
  contactNo: number;
  hospitalAddress: Address;
  pincode: number;
}

export interface UpdateHospital {
  contactNo: number;
  hospitalAddress: Address;
  pincode: number;
}
