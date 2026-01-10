import api from "./axios";

/* =========================
   Types
========================= */

export interface AdminUser {
  id: string;
  username: string;
  role: "ADMIN" | "RM" | "ANALYST";
  active: boolean;
}

/* =========================
   API Calls
========================= */

// 1️⃣ Get all users (ADMIN only)
export const getAllUsers = async (): Promise<AdminUser[]> => {
  const response = await api.get<AdminUser[]>("/api/admin/users");
  return response.data;
};

// 2️⃣ Update user active status
export const updateUserStatus = async (
  userId: string,
  active: boolean
): Promise<AdminUser> => {
  const response = await api.put<AdminUser>(
    `/api/admin/users/${userId}/status`,
    null,
    {
      params: { active },
    }
  );
  return response.data;
};
