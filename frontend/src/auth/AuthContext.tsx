import { createContext, useContext, useState } from "react";
import type { ReactNode } from "react";
import type { AuthUser } from "../types/auth";

interface AuthContextValue {
  token: string | null;
  user: AuthUser | null;
  login: (token: string, user: AuthUser) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(
    localStorage.getItem("token")
  );
  const [user, setUser] = useState<AuthUser | null>(() => {
  try {
    const stored = localStorage.getItem("user");

    if (!stored || stored === "undefined") {
      return null;
    }

    return JSON.parse(stored) as AuthUser;
  } catch (error) {
    console.error("Failed to parse user from localStorage", error);
    return null;
  }
});


  const login = (token: string, user: AuthUser) => {
    localStorage.setItem("token", token);
    localStorage.setItem("user", JSON.stringify(user));
    setToken(token);
    setUser(user);
  };

  const logout = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("user");
  setToken(null);
  setUser(null);
};


  return (
    <AuthContext.Provider value={{ token, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return context;
}
