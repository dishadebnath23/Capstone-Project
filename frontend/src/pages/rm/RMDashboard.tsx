import { useState } from "react";
import {
  Box,
  Typography,
  Paper,
  Stack,
  Divider,
} from "@mui/material";

import CreateClient from "./CreateClient";
import MyClients from "./MyClients";
import CreateCreditRequest from "./CreateCreditRequest";
import MyCreditRequests from "./MyCreditRequests";

export default function RMDashboard() {
  const [refreshKey, setRefreshKey] = useState(0);

  const refreshClients = () => {
    setRefreshKey((prev) => prev + 1);
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        backgroundColor: "#e3f2fd", // light blue background
        p: 4,
      }}
    >
      {/* PAGE HEADER */}
      <Typography variant="h4" fontWeight={600} mb={1}>
        Relationship Manager Dashboard
      </Typography>

      <Typography variant="body2" color="text.secondary" mb={4}>
        Onboard corporate clients and manage credit requests
      </Typography>

      <Stack spacing={4}>
        {/* ================= CREATE CLIENT ================= */}
        <Paper elevation={3} sx={{ p: 3, borderRadius: 3 }}>
          <Typography variant="h6" fontWeight={600} mb={2}>
            Create Corporate Client
          </Typography>

          <CreateClient onClientCreated={refreshClients} />
        </Paper>

        {/* ================= MY CLIENTS ================= */}
        <Paper elevation={3} sx={{ p: 3, borderRadius: 3 }}>
          <Typography variant="h6" fontWeight={600} mb={2}>
            My Clients
          </Typography>

          <MyClients key={refreshKey} />
        </Paper>

        <Divider />

        {/* ================= CREATE CREDIT REQUEST ================= */}
        <Paper elevation={3} sx={{ p: 3, borderRadius: 3 }}>
          <Typography variant="h6" fontWeight={600} mb={2}>
            Create Credit Request
          </Typography>

          <CreateCreditRequest />
        </Paper>

        {/* ================= MY CREDIT REQUESTS ================= */}
        <Paper elevation={3} sx={{ p: 3, borderRadius: 3 }}>
          <Typography variant="h6" fontWeight={600} mb={2}>
            My Credit Requests
          </Typography>

          <MyCreditRequests />
        </Paper>
      </Stack>
    </Box>
  );
}
