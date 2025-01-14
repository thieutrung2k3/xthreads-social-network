import React, { useState } from "react";
import "./css/Register.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { getAccountIDFromToken, useTokenValidation } from "./Utils/AuthUntil";

export default function ChangePassword() {
  useTokenValidation();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    oldPassword: "",
    password: "",
  });
  const token = localStorage.getItem("token");
  const accountID = getAccountIDFromToken(token);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.put(
        `http://localhost:8888/api/auth/account/update/password/${accountID}`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`, // Gửi token trong Authorization header
            "Content-Type": "application/json",
          },
        }
      );

      if (response.data.code === 1000) {
        alert("Change successful.");
        setFormData({
          oldPassword: "",
          password: "",
        });
        navigate("/");
      } else {
        alert("Change failed.");
      }
    } catch (error) {
      alert("Failed to Change. Please try again later.");
    }
  };

  return (
    <>
      <div className="container">
        <h1>Change Password</h1>
        <div className="underline" />
        <form onSubmit={handleSubmit}>
          <label htmlFor="oldPassword">Old password*</label>
          <input
            type="password"
            name="oldPassword"
            placeholder="Enter Your Old Password"
            value={formData.oldPassword}
            onChange={handleChange}
            required
          />
          <label htmlFor="password">New password*</label>
          <input
            type="password"
            name="password"
            placeholder="Enter Your New Password"
            value={formData.password}
            onChange={handleChange}
            required
          />
          <button type="submit">Submit</button>

          <label className="redirect">Forgot your password?</label>
        </form>
      </div>
    </>
  );
}
