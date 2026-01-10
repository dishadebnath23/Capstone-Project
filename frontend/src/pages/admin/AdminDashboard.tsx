import { useEffect, useState } from "react";
import {
  Box,
  Typography,
  CircularProgress,
  Divider,
  Paper,
  Container,
  Stack,
} from "@mui/material";
import { getAllUsers } from "../../api/adminApi";
import type { AdminUser } from "../../api/adminApi";
import UserTable from "./UserTable";
import CreateUserForm from "./CreateUserForm";

export default function AdminDashboard() {
  const [users, setUsers] = useState<AdminUser[]>([]);
  const [loading, setLoading] = useState(true);

  const loadUsers = async () => {
    try {
      setLoading(true);
      const data = await getAllUsers();
      setUsers(data);
    } catch (error) {
      console.error("Failed to fetch users", error);
      alert("Failed to load users");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUsers();
  }, []);

  return (
    <Box sx={{ minHeight: "100vh", backgroundColor: "#e3f2fd" }}>

      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Typography variant="h4" fontWeight={600} gutterBottom>
          Admin Dashboard
        </Typography>

        <Typography variant="body2" color="text.secondary" mb={3}>
          Manage users, roles, and account status
        </Typography>

        <Stack spacing={4}>
          {/* ➕ CREATE USER */}
          <Paper elevation={3} sx={{ p: 3, borderRadius: 2 }}>
  <Typography variant="h6" fontWeight={600} mb={2} align="center">
    Create User
  </Typography>

  <Box sx={{ display: "flex", justifyContent: "center" }}>
    <Box sx={{ width: 420 }}>
      <CreateUserForm onUserCreated={loadUsers} />
    </Box>
  </Box>
</Paper>


          {/* 👥 USERS TABLE */}
          <Paper elevation={3} sx={{ p: 3, borderRadius: 2 }}>
            <Typography variant="h6" fontWeight={600} mb={2}>
              Users
            </Typography>

            <Divider sx={{ mb: 2 }} />

            {loading ? (
              <CircularProgress />
            ) : (
              <UserTable users={users} onStatusChange={loadUsers} />
            )}
          </Paper>
        </Stack>
      </Container>
    </Box>
  );
}
