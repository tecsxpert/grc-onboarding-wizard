import { useState } from "react";
import API from "../services/api";

function LoginPage() {
  const [user, setUser] = useState({
    username: "",
    password: "",
  });

  const handleChange = (e) => {
    setUser({
      ...user,
      [e.target.name]: e.target.value,
    });
  };

  const handleLogin = async () => {
    try {
      const res = await API.post("/auth/login", user);

      if (res.data === "Login successful") {
        // ✅ store login state
        localStorage.setItem("isLoggedIn", "true");

        // ✅ redirect to home
        window.location.href = "/home";
      } else {
        alert("Invalid credentials");
      }
    } catch (err) {
      console.error("Login error:", err);
    }
  };

  return (
    <div className="flex items-center justify-center h-screen">
      <div className="bg-white p-6 shadow rounded w-80">
        <h2 className="text-xl font-bold mb-4 text-center">Login</h2>

        <input
          name="username"
          placeholder="Username"
          className="w-full p-2 mb-3 border rounded"
          onChange={handleChange}
          value={user.username}
        />

        <input
          name="password"
          type="password"
          placeholder="Password"
          className="w-full p-2 mb-3 border rounded"
          onChange={handleChange}
          value={user.password}
        />

        <button
          className="w-full bg-blue-500 text-white p-2 rounded"
          onClick={handleLogin}
        >
          Login
        </button>
      </div>
    </div>
  );
}

export default LoginPage;