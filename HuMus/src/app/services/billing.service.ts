import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { BillingRequest, Billing, PaymentDetails } from '../models/billing.requests';

@Injectable({
  providedIn: 'root',
})
export class BillingService {
  
  private baseUrl = 'http://localhost:8080/api/billings';

  constructor(private httpClient: HttpClient) {}

  addBilling(authorizationHeader: string, billingRequest: BillingRequest): Observable<Billing> {
    const headers = new HttpHeaders({ 'Authorization': authorizationHeader });
    return this.httpClient.post<Billing>(`${this.baseUrl}/add`, billingRequest, { headers });
  }
  findByBookingId(bookingId: string): Observable<Billing>{
    return this.httpClient.get<Billing>(`${this.baseUrl}/findByBookingId/${bookingId}`);
  }

  findBillingById(billingId: string): Observable<Billing> {
    return this.httpClient.get<Billing>(`${this.baseUrl}/findById/${billingId}`);
  }

  initiatePayment(billingId: string): Observable<PaymentDetails> {
    return this.httpClient.get<PaymentDetails>(`${this.baseUrl}/pay/${billingId}`);
  }

  successPay(billingId: string): Observable<Billing> {
    return this.httpClient.get<Billing>(`${this.baseUrl}/pay/success/${billingId}`);
  }
}