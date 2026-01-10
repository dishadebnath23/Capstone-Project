import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import type { JSX } from "react";

export default function PrivateRoute(): JSX.Element {
  const { token } = useAuth();

  return token ? <Outlet /> : <Navigate to="/login" replace />;
}
