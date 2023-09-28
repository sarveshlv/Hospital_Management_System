import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { BillingRequest, Billing } from '../models/billing.requests';

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

  findBillingById(billingId: string): Observable<Billing> {
    return this.httpClient.get<Billing>(`${this.baseUrl}/findById/${billingId}`);
  }

  initiatePayment(billingRequest: BillingRequest): Observable<string> {
    return this.httpClient.post<string>(`${this.baseUrl}/pay`, billingRequest);
  }

  cancelPayment(): Observable<string> {
    return this.httpClient.get<string>(`${this.baseUrl}/pay/cancel`);
  }

  completePayment(paymentId: string, payerId: string): Observable<string> {
    return this.httpClient.get<string>(`${this.baseUrl}/pay/success?paymentId=${paymentId}&PayerID=${payerId}`);
  }
}