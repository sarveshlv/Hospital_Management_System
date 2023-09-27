export class Patient {
  id: string;
  firstName: string;
  lastName: string;
  contactNumber: number;
  aadharCard: number;
  address: Address;

  constructor(
    id: string,
    firstName: string,
    lastName: string,
    contactNumber: number,
    aadharCard: number,
    address: Address
  ) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.contactNumber = contactNumber;
    this.aadharCard = aadharCard;
    this.address = address;
  }
}

export class Address {
  city: string;
  state: string;
  pincode: number;

  constructor(city: string, state: string, pincode: number) {
    this.city = city;
    this.state = state;
    this.pincode = pincode;
  }
}

export enum HospitalType {
  GOVT = 'GOVT',
  PRIVATE = 'PRIVATE',
  SEMI_PRIVATE = 'SEMI_PRIVATE',
}

export interface AddPatientRequest {
  firstName: string;
  lastName: string;
  contactNumber: number;
  aadharNumber: number;
  address: Address;
}