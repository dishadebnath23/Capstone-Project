import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Button,
} from "@mui/material";
import { useState } from "react";
import { updateClient } from "../../api/rmClientApi";


interface Props {
  client: any;
  onClose: () => void;
  onUpdated: () => void;
}

export default function EditClientDialog({
  client,
  onClose,
  onUpdated,
}: Props) {
  const [form, setForm] = useState({
    companyName: client.companyName,
    industry: client.industry,
    address: client.address,
    primaryContactName: client.primaryContact?.name || "",
    primaryContactEmail: client.primaryContact?.email || "",
    primaryContactPhone: client.primaryContact?.phone || "",
    annualTurnover: client.annualTurnover,
    documentsSubmitted: client.documentsSubmitted,
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const handleSave = async () => {
    await updateClient(client.id, form);
    onUpdated();
    onClose();
  };

  return (
    <Dialog open onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>Edit Client</DialogTitle>

      <DialogContent>
        <TextField fullWidth margin="normal" label="Company Name" name="companyName" value={form.companyName} onChange={handleChange} />
        <TextField fullWidth margin="normal" label="Industry" name="industry" value={form.industry} onChange={handleChange} />
        <TextField fullWidth margin="normal" label="Address" name="address" value={form.address} onChange={handleChange} />
        <TextField fullWidth margin="normal" label="Primary Contact Name" name="primaryContactName" value={form.primaryContactName} onChange={handleChange} />
        <TextField fullWidth margin="normal" label="Primary Contact Email" name="primaryContactEmail" value={form.primaryContactEmail} onChange={handleChange} />
        <TextField fullWidth margin="normal" label="Primary Contact Phone" name="primaryContactPhone" value={form.primaryContactPhone} onChange={handleChange} />
        <TextField fullWidth margin="normal" label="Annual Turnover" name="annualTurnover" type="number" value={form.annualTurnover} onChange={handleChange} />
      </DialogContent>

      <DialogActions>
        <Button onClick={onClose}>Cancel</Button>
        <Button variant="contained" onClick={handleSave}>
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
}
