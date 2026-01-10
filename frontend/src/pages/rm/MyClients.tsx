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
  TextField,
  Button,
} from "@mui/material";
import api from "../../api/axios";
import EditClientDialog from "./EditClientDialog";

export default function MyClients() {
  const [clients, setClients] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  const [companyName, setCompanyName] = useState("");
  const [industry, setIndustry] = useState("");

  const [selectedClient, setSelectedClient] = useState<any | null>(null);

  const loadClients = async () => {
    setLoading(true);
    try {
      const res = await api.get("/api/rm/clients");
      setClients(res.data);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    setLoading(true);
    try {
      const res = await api.get("/api/rm/clients/search", {
        params: {
          companyName: companyName || undefined,
          industry: industry || undefined,
        },
      });
      setClients(res.data);
    } finally {
      setLoading(false);
    }
  };

  const clearFilters = () => {
    setCompanyName("");
    setIndustry("");
    loadClients();
  };

  useEffect(() => {
    loadClients();
  }, []);

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h5" mb={2}>
        My Clients
      </Typography>

      {/* 🔍 SEARCH / FILTER */}
      <Box sx={{ display: "flex", gap: 2, mb: 3 }}>
        <TextField
          label="Company Name"
          value={companyName}
          onChange={(e) => setCompanyName(e.target.value)}
        />
        <TextField
          label="Industry"
          value={industry}
          onChange={(e) => setIndustry(e.target.value)}
        />
        <Button variant="contained" onClick={handleSearch}>
          Search
        </Button>
        <Button variant="outlined" onClick={clearFilters}>
          Clear
        </Button>
      </Box>

      {loading ? (
        <CircularProgress />
      ) : (
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Company</TableCell>
              <TableCell>Industry</TableCell>
              <TableCell>Turnover</TableCell>
              <TableCell>Documents</TableCell>
              <TableCell>Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {clients.map((c) => (
              <TableRow key={c.id}>
                <TableCell>{c.companyName}</TableCell>
                <TableCell>{c.industry}</TableCell>
                <TableCell>{c.annualTurnover}</TableCell>
                <TableCell>
                  {c.documentsSubmitted ? "Submitted" : "Pending"}
                </TableCell>
                <TableCell>
                  <Button
                    size="small"
                    variant="outlined"
                    onClick={() => setSelectedClient(c)}
                  >
                    Edit
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      )}

      {/* ✏️ EDIT CLIENT DIALOG */}
      {selectedClient && (
        <EditClientDialog
          client={selectedClient}
          onClose={() => setSelectedClient(null)}
          onUpdated={loadClients}
        />
      )}
    </Box>
  );
}
