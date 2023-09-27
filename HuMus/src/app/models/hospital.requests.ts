export class Hospital {
  id: string;
  name: string;
  verified: boolean;
  hospitalType: HospitalType;
  contactNo: number;
  address: Address;

  constructor(
    id: string,
    name: string,
    verified: boolean,
    hospitalType: HospitalType,
    contactNo: number,
    address: Address
  ) {
    this.id = id;
    this.name = name;
    this.verified = verified;
    this.hospitalType = hospitalType;
    this.contactNo = contactNo;
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
export interface AddHospitalRequest {
  name: string;
  hospitalType: HospitalType;
  contactNo: number;
  address: Address;
}
