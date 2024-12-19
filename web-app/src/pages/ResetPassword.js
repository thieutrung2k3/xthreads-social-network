import React, { useState } from "react";
import { fetchUserInfo, useTokenValidation } from "./Utils/AuthUntil.js";
import { useNavigate } from "react-router-dom";
import axios from "axios";

export default function ResetPassword() {
  useTokenValidation(() => {
    console.log("Token is valid.");
  });

  return (
    <>
      <form className="container">
        <h1>Change Password</h1>
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
