import { useState } from "react";
import {
  Box,
  Button,
  TextField,
  Typography,
  Checkbox,
  FormControlLabel,
} from "@mui/material";
import api from "../../api/axios";

interface Props {
  onClientCreated: () => void;
}

export default function CreateClient({ onClientCreated }: Props) {
  const [form, setForm] = useState({
    companyName: "",
    industry: "",
    address: "",
    primaryContactName: "",
    primaryContactEmail: "",
    primaryContactPhone: "",
    annualTurnover: "",
    documentsSubmitted: false,
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, documentsSubmitted: e.target.checked });
  };

  const handleSubmit = async () => {
    const {
      companyName,
      industry,
      primaryContactEmail,
      annualTurnover,
    } = form;

    // SPEC VALIDATIONS
    if (!companyName || !industry || !primaryContactEmail || !annualTurnover) {
      alert("Please fill all required fields");
      return;
    }

    if (Number(annualTurnover) <= 0) {
      alert("Annual turnover must be greater than 0");
      return;
    }

    try {
      await api.post("/api/rm/clients", {
        companyName: form.companyName,
        industry: form.industry,
        address: form.address,
        primaryContactName: form.primaryContactName,
        primaryContactEmail: form.primaryContactEmail,
        primaryContactPhone: form.primaryContactPhone,
        annualTurnover: Number(form.annualTurnover),
        documentsSubmitted: form.documentsSubmitted,
      });

      alert("Client created successfully");

      // 🔁 REFRESH CLIENT LIST
      onClientCreated();

      // CLEAR FORM
      setForm({
        companyName: "",
        industry: "",
        address: "",
        primaryContactName: "",
        primaryContactEmail: "",
        primaryContactPhone: "",
        annualTurnover: "",
        documentsSubmitted: false,
      });
    } catch (err) {
      console.error(err);
      alert("Failed to create client");
    }
  };

  return (
    <Box sx={{ maxWidth: 500 }}>
      <Typography variant="h5" mb={2}>
        Create Corporate Client
      </Typography>

      <TextField label="Company Name *" name="companyName" fullWidth margin="normal" value={form.companyName} onChange={handleChange} />
      <TextField label="Industry *" name="industry" fullWidth margin="normal" value={form.industry} onChange={handleChange} />
      <TextField label="Address" name="address" fullWidth margin="normal" value={form.address} onChange={handleChange} />
      <TextField label="Primary Contact Name" name="primaryContactName" fullWidth margin="normal" value={form.primaryContactName} onChange={handleChange} />
      <TextField label="Primary Contact Email *" name="primaryContactEmail" fullWidth margin="normal" value={form.primaryContactEmail} onChange={handleChange} />
      <TextField label="Primary Contact Phone" name="primaryContactPhone" fullWidth margin="normal" value={form.primaryContactPhone} onChange={handleChange} />
      <TextField label="Annual Turnover *" name="annualTurnover" type="number" fullWidth margin="normal" value={form.annualTurnover} onChange={handleChange} />

      <FormControlLabel
        control={<Checkbox checked={form.documentsSubmitted} onChange={handleCheckboxChange} />}
        label="Documents Submitted"
      />

      <Button variant="contained" fullWidth sx={{ mt: 2 }} onClick={handleSubmit}>
        Create Client
      </Button>
    </Box>
  );



}



