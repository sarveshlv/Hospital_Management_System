export class Patient {
  patientId: string;
  firstName: string;
  lastName: string;
  contactNumber: number;
  aadharCard: number;
  address: Address;
  pincode: number;

  constructor(
    patientId: string,
    firstName: string,
    lastName: string,
    contactNumber: number,
    aadharCard: number,
    address: Address,
    pincode: number
  ) {
    this.patientId = patientId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.contactNumber = contactNumber;
    this.aadharCard = aadharCard;
    this.address = address;
    this.pincode = pincode;
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

export interface AddPatient {
  firstName: string;
  lastName: string;
  contactNumber: number;
  aadharNumber: number;
  address: Address;
  pincode: number;
}
