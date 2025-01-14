import React, { useState } from "react";
import "./css/Register.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import styles from "./Popup.module.css"; // Sử dụng CSS Modules

const EditProfile = ({ show, close, userInfo }) => {
  const handleCancel = () => {
    if (close) close(); // Đóng popup
  };
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstName: userInfo.firstName,
    lastName: userInfo.lastName,
    email: userInfo.email,
    address: userInfo.address,
    dob: userInfo.dob,
    gender: userInfo.gender,
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.put(
        `http://localhost:8888/api/user/information/update/user/${userInfo.accountID}`,
        formData,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (response.data.code === 1000) {
        alert("Update successful.");
        setFormData({
          firstName: "",
          lastName: "",
          email: "",
          address: "",
          dob: "",
          gender: "",
        });
        setTimeout(() => {
          window.location.reload(); // Reload trang
        }, 1000);
      } else {
        alert("Update failed.");
      }
    } catch (error) {
      alert("Failed to update. Please try again later.");
    }
  };
  if (!show) return null;

  return (
    <div className={styles.modalOverlay}>
      <div className="container">
        <h1>Change information</h1>
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

          <button type="submit">Submit</button>
          <button
            type="submit"
            style={{ background: "#d6d9dd", color: "black" }}
            onClick={handleCancel}
          >
            Cancel
          </button>
        </form>
      </div>
    </div>
  );
};

export default EditProfile;
