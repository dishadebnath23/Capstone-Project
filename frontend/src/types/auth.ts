// Roles
export type UserRole = "ADMIN" | "RM" | "ANALYST";

// Full backend user (DB model)
export interface User {
  id: string;
  username: string;
  email: string;
  role: UserRole;
  active: boolean;
}

// Lightweight frontend auth user
export interface AuthUser {
  username: string;
  role: UserRole;
}

// Login API response (matches backend)
export interface LoginResponse {
  token: string;
  username: string;
  role: UserRole;
}

// Auth context contract
export interface AuthContextType {
  token: string | null;
  user: AuthUser | null;
  login: (token: string, user: AuthUser) => void;
  logout: () => void;
}
