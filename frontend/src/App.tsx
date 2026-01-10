import { Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import AdminDashboard from "./pages/admin/AdminDashboard";
import RMDashboard from "./pages/rm/RMDashboard";
import AnalystDashboard from "./pages/analyst/AnalystDashboard";
import PrivateRoute from "./Routes/PrivateRoute";
import RoleRoute from "./Routes/RoleRoute";

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />

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

      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
}

export default App;
