import {
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Button,
  Chip,
  Stack,
} from "@mui/material";

import { updateUserStatus } from "../../api/adminApi";
import type { AdminUser } from "../../api/adminApi";

interface UserTableProps {
  users: AdminUser[];
  onStatusChange: () => void;
}

export default function UserTable({
  users,
  onStatusChange,
}: UserTableProps) {
  // ✅ TOGGLE ACTIVE / INACTIVE STATUS
  const handleToggleStatus = async (user: AdminUser) => {
    try {
      await updateUserStatus(user.id, !user.active);
      onStatusChange();
    } catch (error) {
      console.error("Failed to update user status", error);
      alert("Failed to update user status");
    }
  };

  return (

    
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
              {/* USERNAME */}
              <TableCell>{user.username}</TableCell>

              {/* ROLE */}
              <TableCell>
                <Chip
                  label={user.role}
                  color={isAdmin ? "warning" : "primary"}
                />
              </TableCell>

              {/* STATUS */}
              <TableCell>
                <Chip
                  label={user.active ? "Active" : "Inactive"}
                  color={user.active ? "success" : "default"}
                />
              </TableCell>

              {/* ACTION */}
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
                  </Stack>
                )}
              </TableCell>
            </TableRow>
          );
        })}
      </TableBody>
    </Table>
  );
}
