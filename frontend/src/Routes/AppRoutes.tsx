import { Routes, Route } from "react-router-dom";
import Login from "../pages/Login";
import AdminDashboard from "../pages/admin/AdminDashboard";
import RMDashboard from "../pages/rm/RMDashboard";
import AnalystDashboard from "../pages/analyst/AnalystDashboard";
import PrivateRoute from "./PrivateRoute";
import RoleRoute from "./RoleRoute";

export default function AppRoutes() {
  return (
    <Routes>
      {/* PUBLIC */}
      <Route path="/login" element={<Login />} />

      {/* PROTECTED */}
      <Route element={<PrivateRoute />}>
        
        <Route element={<RoleRoute role="ADMIN" />}>
          <Route path="/admin" element={<AdminDashboard />} />
        </Route>

        <Route element={<RoleRoute role="RM" />}>
          <Route path="/rm" element={<RMDashboard />} />
        </Route>

        <Route element={<RoleRoute role="ANALYST" />}>
          <Route path="/analyst" element={<AnalystDashboard />} />
        </Route>

      </Route>
    </Routes>
  );
}
