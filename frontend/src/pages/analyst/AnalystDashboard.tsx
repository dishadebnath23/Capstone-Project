import { Box, Typography, Paper } from "@mui/material";
import AllCreditRequests from "./AllCreditRequests";

export default function AnalystDashboard() {
  return (
    <Box
      sx={{
        minHeight: "100vh",
        backgroundColor: "#e3f2fd", // same light blue as other dashboards
        p: 4,
      }}
    >
      {/* PAGE HEADER */}
      <Typography variant="h4" fontWeight={600} mb={1}>
        Analyst Dashboard
      </Typography>

      <Typography variant="body2" color="text.secondary" mb={4}>
        Review and approve corporate credit requests
      </Typography>

      {/* CONTENT CARD */}
      <Paper elevation={3} sx={{ p: 3, borderRadius: 3 }}>
        <AllCreditRequests />
      </Paper>
    </Box>
  );
}
