export class Billing {
  id: string;
  bookingId: string;
  billAmount: number;
  paymentStatus: PaymentStatus;

  constructor(
    id: string,
    bookingId: string,
    billAmount: number,
    paymentStatus: PaymentStatus
  ) {
    this.id = id;
    this.bookingId = bookingId;
    this.billAmount = billAmount;
    this.paymentStatus = paymentStatus;
  }
}

export interface PaymentDetails {
  id: string;
  keyId: string;
  billingId: string;
  amount: number;
  currency: string;
}

export enum PaymentStatus {
  PENDING = 'PENDING',
  COMPLETED = 'COMPLETED',
}
export interface BillingRequest {
  bookingId: string;
}
