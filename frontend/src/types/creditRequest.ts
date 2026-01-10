// src/types/creditRequest.ts

export type CreditRequestStatus =
  | "PENDING"
  | "APPROVED"
  | "REJECTED";

export interface CreditRequest {
  id: string;
  clientId: string;
  rmUsername: string;

  requestedAmount: number;
  tenureMonths: number;
  purpose: string;

  status: CreditRequestStatus;

  createdAt: string;
  reviewedAt?: string;

  analystUsername?: string;
  analystRemark?: string;
}
