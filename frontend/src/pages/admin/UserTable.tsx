import {
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Button,
  Chip,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  MenuItem,
  Stack,
} from "@mui/material";
import { useState } from "react";
import { updateUserStatus } from "../../api/adminApi";
import api from "../../api/axios";
import type { AdminUser } from "../../api/adminApi";

interface UserTableProps {
  users: AdminUser[];
  onStatusChange: () => void;
  onUserUpdated?: () => void; // ✅ IMPORTANT (fixes red line)
}

export default function UserTable({
  users,
  onStatusChange,
  onUserUpdated,
}: UserTableProps) {
  const [selectedUser, setSelectedUser] = useState<AdminUser | null>(null);
 
  const [role, setRole] = useState("");

  // ✅ EXISTING STATUS TOGGLE (UNCHANGED)
  const handleToggleStatus = async (user: AdminUser) => {
    try {
      await updateUserStatus(user.id, !user.active);
      onStatusChange();
    } catch (error) {
      console.error("Failed to update user status", error);
      alert("Failed to update user status");
    }
  };

  // ✅ OPEN EDIT DIALOG
  const openEdit = (user: AdminUser) => {
    setSelectedUser(user);
   
    setRole(user.role);
  };

  // ✅ SAVE EDIT
  const handleSaveEdit = async () => {
    if (!selectedUser) return;

    try {
      await api.put(`/api/admin/users/${selectedUser.id}`, {
      
        role,
      });
      setSelectedUser(null);
      onUserUpdated?.(); // refresh table
    } catch (error) {
      console.error("Failed to update user", error);
      alert("Failed to update user");
    }
  };

  return (
    <>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Username</TableCell>
            <TableCell>Role</TableCell>
            <TableCell>Status</TableCell>
            <TableCell>Action</TableCell>
          </TableRow>
        </TableHead>

        <TableBody>
          {users.map((user) => {
            const isAdmin = user.role === "ADMIN";

            return (
              <TableRow key={user.id}>
                <TableCell>{user.username}</TableCell>

                <TableCell>
                  <Chip
                    label={user.role}
                    color={isAdmin ? "warning" : "primary"}
                  />
                </TableCell>

                <TableCell>
                  <Chip
                    label={user.active ? "Active" : "Inactive"}
                    color={user.active ? "success" : "default"}
                  />
                </TableCell>

                {/* 🔐 ADMIN PROTECTION */}
                <TableCell>
                  {isAdmin ? (
                    <span style={{ color: "#888", fontWeight: 500 }}>
                      Admin (protected)
                    </span>
                  ) : (
                    <Stack direction="row" spacing={1}>
                      <Button
                        variant="contained"
                        color={user.active ? "error" : "success"}
                        onClick={() => handleToggleStatus(user)}
                      >
                        {user.active ? "Deactivate" : "Activate"}
                      </Button>

                      <Button
                        variant="outlined"
                        onClick={() => openEdit(user)}
                      >
                        Edit
                      </Button>
                    </Stack>
                  )}
                </TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>

      {/* ✏️ EDIT USER DIALOG */}
      <Dialog open={!!selectedUser} onClose={() => setSelectedUser(null)}>
        <DialogTitle>Edit User</DialogTitle>

        <DialogContent sx={{ minWidth: 350 }}>
          

          <TextField
            label="Role"
            select
            fullWidth
            margin="normal"
            value={role}
            onChange={(e) => setRole(e.target.value)}
          >
            <MenuItem value="RM">RM</MenuItem>
            <MenuItem value="ANALYST">ANALYST</MenuItem>
          </TextField>
        </DialogContent>

        <DialogActions>
          <Button onClick={() => setSelectedUser(null)}>Cancel</Button>
          <Button variant="contained" onClick={handleSaveEdit}>
            Save
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
