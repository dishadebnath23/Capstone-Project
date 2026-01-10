import { useEffect, useState } from "react";
import {
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Typography,
  Box,
  Chip,
  CircularProgress,
} from "@mui/material";
import api from "../../api/axios";

interface CreditRequest {
  id: string;
  clientId: string;
  requestedAmount: number;
  tenureMonths: number;
  status: "PENDING" | "APPROVED" | "REJECTED";
  analystRemark?: string;
}

export default function MyCreditRequests() {
  const [requests, setRequests] = useState<CreditRequest[]>([]);
  const [loading, setLoading] = useState(true);

 const loadRequests = async () => {
  try {
    const res = await api.get<CreditRequest[]>("/api/credit-requests/rm");
    setRequests(res.data);
  } catch {
    alert("Failed to load credit requests");
  } finally {
    setLoading(false);
  }
};


  useEffect(() => {
    loadRequests();
  }, []);

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 5 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h5" mb={2}>
        My Credit Requests
      </Typography>

      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Client</TableCell>
            <TableCell>Amount</TableCell>
            <TableCell>Tenure</TableCell>
            <TableCell>Status</TableCell>
            <TableCell>Analyst Remark</TableCell>
          </TableRow>
        </TableHead>

        <TableBody>
          {requests.map((r) => (
            <TableRow key={r.id}>
              <TableCell>{r.clientId.slice(-4)}</TableCell>
              <TableCell>{r.requestedAmount}</TableCell>
              <TableCell>{r.tenureMonths}</TableCell>
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
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Box>
  );
}
