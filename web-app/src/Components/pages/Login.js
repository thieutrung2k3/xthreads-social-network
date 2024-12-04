import React, { useState, useEffect } from "react";
import "./css/Login.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";

export default function Login() {
  const navigate = useNavigate();

  useEffect(() => {
    const checkToken = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        return;
      }

      try {
        // Gửi token qua phương thức POST trong body yêu cầu
        const response = await axios.post(
          "http://localhost:8888/api/auth/t/validate", // URL của API xác minh token
          { token }, // Gửi token trong body
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        // Kiểm tra phản hồi từ API
        if (
          response.data.code !== 1000 ||
          response.data.result.result === false
        ) {
          // Token không hợp lệ, xoá token và điều hướng về login
          localStorage.removeItem("token");
        } else {
          navigate("/");
        }
      } catch (error) {
        // Xử lý khi gọi API thất bại
        alert("Token validation failed:" + error);
        localStorage.removeItem("token");
      }
    };

    checkToken(); // Gọi hàm kiểm tra token khi component được render
  }, [navigate]);

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
        setError(error.response.data.message || "Login failed.");
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
