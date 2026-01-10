// src/api/rmClientApi.ts

import api from "./axios";
import type { Client } from "../types/client";

// ================================
// RM: Get my clients (ALREADY EXISTS – DO NOT TOUCH)
// ================================
export const getMyClients = async (): Promise<Client[]> => {
  const res = await api.get<Client[]>("/api/clients");
  return res.data;
};

// ================================
// RM: Get single client by ID (ALREADY EXISTS – DO NOT TOUCH)
// ================================
export const getClientById = async (id: string): Promise<Client> => {
  const res = await api.get<Client>(`/api/rm/clients/${id}`);
  return res.data;
};

// =====================================================
// ✅ ADD THIS — RM: Search / Filter clients
// =====================================================
export const searchClients = async (
  companyName?: string,
  industry?: string
): Promise<Client[]> => {
  const res = await api.get<Client[]>("/api/rm/clients/search", {
    params: {
      companyName: companyName || undefined,
      industry: industry || undefined,
    },
  });
  return res.data;
};

// =====================================================
// ✅ ADD THIS — RM: Update client
// =====================================================
export const updateClient = async (
  id: string,
  data: Partial<Client>
): Promise<Client> => {
  const res = await api.put<Client>(`/api/rm/clients/${id}`, data);
  return res.data;
};


