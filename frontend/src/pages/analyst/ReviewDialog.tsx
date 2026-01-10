import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  MenuItem,
} from "@mui/material";
import { useState } from "react";
import api from "../../api/axios";

interface Props {
  open: boolean;
  requestId: string | null;
  onClose: () => void;
  onSuccess: () => void; // refresh table after update
}

export default function ReviewDialog({
  open,
  requestId,
  onClose,
  onSuccess,
}: Props) {
  const [status, setStatus] = useState<"APPROVED" | "REJECTED">("APPROVED");
  const [remark, setRemark] = useState("");
  const [submitting, setSubmitting] = useState(false);

  const handleSubmit = async () => {
    // FRONTEND VALIDATION
    if (!remark.trim()) {
      alert("Remark is required");
      return;
    }

    if (!requestId) return;

    try {
      setSubmitting(true);

      await api.put(
        `/api/credit-requests/${requestId}/review`,
        null,
        {
          params: {
            status,
            remark,
          },
        }
      );

      alert(`Request ${status.toLowerCase()} successfully`);

      // reset & close
      setRemark("");
      setStatus("APPROVED");

      onSuccess();
      onClose();
    } catch (err) {
      console.error(err);
      alert("Failed to update request");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>Review Credit Request</DialogTitle>

      <DialogContent>
        <TextField
          select
          label="Decision"
          fullWidth
          margin="normal"
          value={status}
          onChange={(e) =>
            setStatus(e.target.value as "APPROVED" | "REJECTED")
          }
        >
          <MenuItem value="APPROVED">Approve</MenuItem>
          <MenuItem value="REJECTED">Reject</MenuItem>
        </TextField>

        <TextField
          label="Remark *"
          fullWidth
          margin="normal"
          multiline
          minRows={3}
          value={remark}
          onChange={(e) => setRemark(e.target.value)}
        />
      </DialogContent>

      <DialogActions>
        <Button onClick={onClose} disabled={submitting}>
          Cancel
        </Button>
        <Button
          variant="contained"
          onClick={handleSubmit}
          disabled={submitting}
        >
          Submit
        </Button>
      </DialogActions>
    </Dialog>
  );
}
