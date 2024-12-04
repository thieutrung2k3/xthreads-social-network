import React, { useState } from "react";
import "./css/Register.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const navigate = useNavigate();
  const handleLogin = () => {
    navigate("/login");
  };
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    username: "",
    password: "",
    urlProfilePicture: "",
    address: "",
    dob: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        "http://localhost:8888/api/auth/account/register",
        formData,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === 1000) {
        alert("Register successful.");
        setFormData({
          firstName: "",
          lastName: "",
          email: "",
          username: "",
          password: "",
          urlProfilePicture: "",
          address: "",
          dob: "",
        });
      } else {
        alert("Register failed.");
      }
    } catch (error) {
      if (error.response) {
        // Nếu có lỗi từ phía server
        alert(
          `Error: ${error.response.data.message || "Something went wrong"}`
        );
      } else {
        // Lỗi khi gửi yêu cầu
        alert("Failed to register. Please try again later.");
      }
    }
  };
  return (
    <>
      <div className="container">
        <h1>Create a new account</h1>
        <p>It's quick and easy.</p>
        <div className="underline" />
        <form onSubmit={handleSubmit}>
          <div className="group-name">
            <div>
              <label htmlFor="firstName">First Name*</label>
              <input
                type="text"
                name="firstName"
                placeholder="Enter Your First Name"
                value={formData.firstName}
                onChange={handleChange}
                required
              />
            </div>
            <div>
              <label htmlFor="lastName">Last Name*</label>
              <input
                type="text"
                name="lastName"
                placeholder="Enter Your Last Name"
                value={formData.lastName}
                onChange={handleChange}
                required
              />
            </div>
          </div>
          <label htmlFor="email">Email*</label>
          <input
            type="email"
            name="email"
            placeholder="Enter Your Email"
            value={formData.email}
            onChange={handleChange}
            required
          />
          <label htmlFor="address">Username*</label>
          <input
            type="text"
            name="username"
            placeholder="Enter Your Username"
            value={formData.username}
            onChange={handleChange}
            required
          />
          <label htmlFor="password">Password*</label>
          <input
            type="password"
            name="password"
            placeholder="Enter Your Password"
            value={formData.password}
            onChange={handleChange}
            required
          />

          <label htmlFor="urlProfilePicture">Avatar*</label>
          <input
            type="file"
            name="urlProfilePicture"
            value={formData.urlProfilePicture}
            onChange={handleChange}
            required
          />

          <label>Gender*</label>
          <div className="gender-options">
            <input
              type="radio"
              id="check-male"
              name="gender"
              value="male"
              checked={formData.gender === "male"}
              onChange={handleChange}
              required
            />
            <label for="check-male">Male</label>
            <input
              type="radio"
              id="check-female"
              name="gender"
              value="female"
              checked={formData.gender === "female"}
              onChange={handleChange}
              required
            />
            <label for="check-female">Female</label>
          </div>

          <label htmlFor="address">Address*</label>
          <input
            type="text"
            name="address"
            placeholder="Enter Your Address"
            value={formData.address}
            onChange={handleChange}
            required
          />

          <label htmlFor="dob">Date of Birthday*</label>
          <input
            type="date"
            name="dob"
            value={formData.dob}
            onChange={handleChange}
            required
          />

          <button type="submit">Sign Up</button>

          <label className="redirect" onClick={handleLogin}>
            Already have an account?
          </label>
        </form>
      </div>
    </>
  );
}
