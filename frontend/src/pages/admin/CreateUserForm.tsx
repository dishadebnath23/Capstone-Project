import { Box, Button, MenuItem, TextField, Typography } from "@mui/material";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import api from "../../api/axios";

const schema = z.object({
  username: z.string().min(3, "Username is required"),
  email: z.string().email("Valid email required"),
  password: z.string().min(6, "Password must be at least 6 characters"),
  role: z.enum(["RM", "ANALYST"]),
});

type CreateUserFormData = z.infer<typeof schema>;

interface CreateUserFormProps {
  onUserCreated: () => void;
}

export default function CreateUserForm({ onUserCreated }: CreateUserFormProps) {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<CreateUserFormData>({
    resolver: zodResolver(schema),
  });

  const onSubmit = async (data: CreateUserFormData) => {
    try {
      await api.post("/api/auth/register", data);
      alert("User created successfully");
      reset();
      onUserCreated();
    } catch (error) {
      console.error("Failed to create user", error);
      alert("Failed to create user");
    }
  };

  return (
    <Box
      component="form"
      onSubmit={handleSubmit(onSubmit)}
      sx={{ mb: 3, maxWidth: 400 }}
    >
      <Typography variant="h6" gutterBottom>
        Create User
      </Typography>

      <TextField
        fullWidth
        label="Username"
        margin="normal"
        {...register("username")}
        error={!!errors.username}
        helperText={errors.username?.message}
      />

      <TextField
        fullWidth
        label="Email"
        margin="normal"
        {...register("email")}
        error={!!errors.email}
        helperText={errors.email?.message}
      />

      <TextField
        fullWidth
        type="password"
        label="Password"
        margin="normal"
        {...register("password")}
        error={!!errors.password}
        helperText={errors.password?.message}
      />

      <TextField
        select
        fullWidth
        label="Role"
        margin="normal"
        defaultValue=""
        {...register("role")}
        error={!!errors.role}
        helperText={errors.role?.message}
      >
        <MenuItem value="RM">RM</MenuItem>
        <MenuItem value="ANALYST">ANALYST</MenuItem>
      </TextField>

      <Button
        type="submit"
        variant="contained"
        color="primary"
        sx={{ mt: 2 }}
        fullWidth
      >
        Create User
      </Button>
    </Box>
  );
}
