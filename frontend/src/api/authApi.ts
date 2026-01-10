import api from "./axios";

export interface LoginResponse {
  token: string;
  username: string;
  role: "ADMIN" | "RM" | "ANALYST";
}

export const loginApi = async (
  username: string,
  password: string
): Promise<LoginResponse> => {
  const response = await api.post<LoginResponse>("/api/auth/login", {
    username,
    password,
  });

  return response.data;
};
