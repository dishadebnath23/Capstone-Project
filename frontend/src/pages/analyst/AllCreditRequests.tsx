import { useEffect, useState } from "react";
import {
  Box,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Typography,
  CircularProgress,
  Button,
  Chip,
} from "@mui/material";
import api from "../../api/axios";
import ReviewDialog from "./ReviewDialog";

interface CreditRequest {
  id: string;
  clientId: string;
  rmUsername: string;
  requestedAmount: number;
  tenureMonths: number;
  purpose: string;
  status: "PENDING" | "APPROVED" | "REJECTED";
  analystRemark?: string;
  createdAt: string;
}

export default function AllCreditRequests() {
  const [requests, setRequests] = useState<CreditRequest[]>([]);
  const [loading, setLoading] = useState(true);

  // 🔹 Dialog state
  const [dialogOpen, setDialogOpen] = useState(false);
  const [selectedRequestId, setSelectedRequestId] = useState<string | null>(
    null
  );

  const loadRequests = async () => {
    try {
      const res = await api.get<CreditRequest[]>(
        "/api/credit-requests/all"
      );
      setRequests(res.data);
    } catch (err) {
      console.error(err);
      alert("Failed to load credit requests");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadRequests();
  }, []);

  const openReviewDialog = (requestId: string) => {
    setSelectedRequestId(requestId);
    setDialogOpen(true);
  };

  const closeReviewDialog = () => {
    setDialogOpen(false);
    setSelectedRequestId(null);
  };

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 6 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h5" mb={2}>
        All Credit Requests (Analyst)
      </Typography>

      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Client</TableCell>
            <TableCell>RM</TableCell>
            <TableCell>Amount</TableCell>
            <TableCell>Tenure</TableCell>
            <TableCell>Purpose</TableCell>
            <TableCell>Status</TableCell>
            <TableCell>Remark</TableCell>
            <TableCell align="center">Action</TableCell>
          </TableRow>
        </TableHead>

        <TableBody>
          {requests.map((r) => (
            <TableRow key={r.id}>
              <TableCell>{r.clientId.slice(-4)}</TableCell>
              <TableCell>{r.rmUsername}</TableCell>
              <TableCell>{r.requestedAmount}</TableCell>
              <TableCell>{r.tenureMonths}</TableCell>
              <TableCell>{r.purpose}</TableCell>
              <TableCell>
                <Chip
                  label={r.status}
                  color={
                    r.status === "APPROVED"
                      ? "success"
                      : r.status === "REJECTED"
                      ? "error"
                      : "warning"
                  }
                />
              </TableCell>
              <TableCell>{r.analystRemark || "-"}</TableCell>
              <TableCell align="center">
                {r.status === "PENDING" ? (
                  <>
                    <Button
                      size="small"
                      variant="contained"
                      color="success"
                      sx={{ mr: 1 }}
                      onClick={() => openReviewDialog(r.id)}
                    >
                      Approve
                    </Button>
                    <Button
                      size="small"
                      variant="contained"
                      color="error"
                      onClick={() => openReviewDialog(r.id)}
                    >
                      Reject
                    </Button>
                  </>
                ) : (
                  <Typography variant="body2" color="text.secondary">
                    Reviewed
                  </Typography>
                )}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>

      {/* 🔹 Review Dialog */}
      <ReviewDialog
        open={dialogOpen}
        requestId={selectedRequestId}
        onClose={closeReviewDialog}
        onSuccess={loadRequests}
      />
    </Box>
  );
}
