import { useEffect, useState } from "react";
import {
  Box,
  Button,
  TextField,
  Typography,
  MenuItem,
} from "@mui/material";
import api from "../../api/axios";
import type { Client } from "../../types/client";

export default function CreateCreditRequest() {
  const [clients, setClients] = useState<Client[]>([]);
  const [form, setForm] = useState({
    clientId: "",
    requestedAmount: "",
    tenureMonths: "",
    purpose: "",
  });

  // LOAD RM CLIENTS
  useEffect(() => {
    api
      .get<Client[]>("/api/rm/clients")
      .then((res) => setClients(res.data))
      .catch(() => alert("Failed to load clients"));
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const handleSubmit = async () => {
    const { clientId, requestedAmount, tenureMonths, purpose } = form;

    // SPEC VALIDATION
    if (!clientId || !requestedAmount || !tenureMonths || !purpose) {
      alert("All fields are required");
      return;
    }

    if (Number(requestedAmount) <= 0 || Number(tenureMonths) <= 0) {
      alert("Amount and tenure must be greater than 0");
      return;
    }

    try {
      await api.post("/api/credit-requests", {
        clientId,
        requestAmount: Number(requestedAmount),
        tenureMonths: Number(tenureMonths),
        purpose,
      });

      alert("Credit request created successfully");

      // CLEAR FORM
      setForm({
        clientId: "",
        requestedAmount: "",
        tenureMonths: "",
        purpose: "",
      });
    } catch {
      alert("Failed to create credit request");
    }
  };

  return (
    <Box sx={{ p: 3, maxWidth: 500 }}>
      <Typography variant="h5" mb={2}>
        Create Credit Request
      </Typography>

      <TextField
        select
        label="Client *"
        name="clientId"
        fullWidth
        margin="normal"
        value={form.clientId}
        onChange={handleChange}
      >
        {clients.map((c) => (
          <MenuItem key={c.id} value={c.id}>
            {c.id.slice(-4)} – {c.companyName}
          </MenuItem>
        ))}
      </TextField>

      <TextField
        label="Requested Amount *"
        name="requestedAmount"
        type="number"
        fullWidth
        margin="normal"
        value={form.requestedAmount}
        onChange={handleChange}
      />

      <TextField
        label="Tenure (months) *"
        name="tenureMonths"
        type="number"
        fullWidth
        margin="normal"
        value={form.tenureMonths}
        onChange={handleChange}
      />

      <TextField
        label="Purpose *"
        name="purpose"
        fullWidth
        margin="normal"
        value={form.purpose}
        onChange={handleChange}
      />

      <Button
        variant="contained"
        fullWidth
        sx={{ mt: 2 }}
        onClick={handleSubmit}
      >
        Submit Credit Request
      </Button>
    </Box>
  );
}
