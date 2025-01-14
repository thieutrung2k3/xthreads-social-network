import React, { useState } from "react";
import "./css/Register.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

export default function Register() {
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
    file: null,
    address: "",
    dob: "",
    gender: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "urlProfilePicture") {
      // Xử lý trường hợp file
      setFormData({ ...formData, file: e.target.files[0] });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const data = new FormData();
    data.append("file", formData.file); // File phải được thêm với key "file"
    data.append(
      "user",
      JSON.stringify({
        firstName: formData.firstName,
        lastName: formData.lastName,
        email: formData.email,
        username: formData.username,
        password: formData.password,
        address: formData.address,
        urlProfilePicture: formData.address,
        dob: formData.dob,
        gender: formData.gender,
      })
    );

    try {
      const response = await axios.post(
        "http://localhost:8888/api/auth/account/register",
        data,
        {
          headers: {
            "Content-Type": "multipart/form-data",
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
          file: null,
          address: "",
          dob: "",
        });
      } else {
        alert("Register failed.");
      }
    } catch (error) {
      alert("Failed to register. Please try again later.");
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
          <label htmlFor="username">Username*</label>
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
            onChange={handleChange}
            required
          />

          <label>Gender*</label>
          <div className="gender-options">
            <input
              type="radio"
              id="check-male"
              name="gender"
              value="Male"
              checked={formData.gender === "Male"}
              onChange={handleChange}
              required
            />
            <label htmlFor="check-male">Male</label>
            <input
              type="radio"
              id="check-female"
              name="gender"
              value="Female"
              checked={formData.gender === "Female"}
              onChange={handleChange}
              required
            />
            <label htmlFor="check-female">Female</label>
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
