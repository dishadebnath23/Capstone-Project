import { Button, TextField, Typography, Box, Paper } from "@mui/material";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { loginApi } from "../api/authApi";
import { useAuth } from "../auth/AuthContext";
import { useNavigate } from "react-router-dom";
import type { AuthUser } from "../types/auth";

const schema = z.object({
  username: z.string().min(3, "Username is required"),
  password: z.string().min(6, "Password must be at least 6 characters"),
});

type LoginForm = z.infer<typeof schema>;

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginForm>({
    resolver: zodResolver(schema),
  });

  const onSubmit = async (data: LoginForm) => {
    try {
      const res = await loginApi(data.username, data.password);

      const user: AuthUser = {
        username: res.username,
        role: res.role,
      };

      login(res.token, user);

      if (res.role === "ADMIN") navigate("/admin");
      else if (res.role === "RM") navigate("/rm");
      else navigate("/analyst");

    } catch {
      alert("Invalid credentials");
    }
  };

  return (
    // 🌈 FULL PAGE BACKGROUND
    <Box
      sx={{
        minHeight: "100vh",
        backgroundColor: "#e3f2fd",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      {/* 🤍 WHITE LOGIN CARD */}
      <Paper
        elevation={6}
        sx={{
          width: 400,
          p: 4,
          borderRadius: 3,
        }}
      >
        {/* 🏦 PROJECT TITLE */}
        <Typography variant="h5" align="center" fontWeight={600} mb={1}>
          Corporate Banking Portal
        </Typography>

        <Typography
          variant="body2"
          align="center"
          color="text.secondary"
          mb={3}
        >
          Secure Login
        </Typography>

        {/* 🔐 LOGIN FORM (UNCHANGED) */}
        <form onSubmit={handleSubmit(onSubmit)}>
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
            type="password"
            label="Password"
            margin="normal"
            {...register("password")}
            error={!!errors.password}
            helperText={errors.password?.message}
          />

          <Button
            fullWidth
            type="submit"
            variant="contained"
            sx={{ mt: 3 }}
          >
            Login
          </Button>
        </form>
      </Paper>
    </Box>
  );
}
