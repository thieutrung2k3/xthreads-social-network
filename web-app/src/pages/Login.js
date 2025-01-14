import React, { useState } from "react";
import "./css/Login.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";

export default function Login() {
  const navigate = useNavigate();

  const handleRegister = () => {
    navigate("/register");
  };

  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault(); // Ngăn form reload lại trang
    try {
      // Gửi yêu cầu đăng nhập tới API
      const response = await axios.post(
        "http://localhost:8888/api/auth/t/login",
        formData,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      // Lấy dữ liệu trả về
      const data = response.data;
      if (data.code === 1000) {
        const token = data.result.token; // Lấy token từ phản hồi
        localStorage.setItem("token", token); // Lưu token vào localStorage
        navigate("/"); // Điều hướng tới trang khác
      } else {
        setError("Invalid response code: " + data.code);
      }
    } catch (error) {
      if (error.response) {
        // Lỗi từ phản hồi của server
        setError("Incorrect password");
      } else {
        // Lỗi khác (như kết nối mạng)
        setError("Something went wrong. Please try again.");
      }
    }
  };

  return (
    <>
      <form className="container">
        <h1>XThreads</h1>
        <div className="underline" />
        <label htmlFor="address">Username</label>
        <input
          type="text"
          name="username"
          placeholder="Enter Your Username"
          value={formData.username}
          onChange={handleChange}
          required
        />
        <label htmlFor="password">Password</label>
        <input
          type="password"
          name="password"
          placeholder="Enter Your Password"
          value={formData.password}
          onChange={handleChange}
          required
        />
        <button type="submit" onClick={handleSubmit}>
          Login
        </button>
        {error && <p className="error-message">{error}</p>} {/* Hiển thị lỗi */}
        <label className="redirect" onClick={handleRegister}>
          Don't have an account? Create now.
        </label>
      </form>
    </>
  );
}
