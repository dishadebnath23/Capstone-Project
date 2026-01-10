// src/types/client.ts

export interface PrimaryContact {
  name: string;
  email: string;
  phone: string;
}

export interface Client {
  id: string;
  companyName: string;
  industry: string;
  address: string;
  annualTurnover: number;
  documentsSubmitted: boolean;
  primaryContact: PrimaryContact;
  rmId: string;
}
