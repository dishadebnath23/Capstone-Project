import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import type { JSX } from "react";

interface RoleRouteProps {
  role: "ADMIN" | "RM" | "ANALYST";
}

export default function RoleRoute({ role }: RoleRouteProps): JSX.Element {
  const { user } = useAuth();

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  return user.role === role ? <Outlet /> : <Navigate to="/login" replace />;
}
