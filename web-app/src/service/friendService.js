import axios from "axios";
import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getAccountIDFromToken } from "../pages/Utils/AuthUntil";

const useFriendRequest = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const { accountID } = useParams();
  console.log(accountID);
  const senderID = getAccountIDFromToken(token);
  const [formData, setFormData] = useState({
    senderId: senderID,
    receiverId: accountID,
  });

  const sendRequest = async () => {
    if (!token) {
      navigate("/"); // Điều hướng về trang chủ nếu không có token
      return false;
    }

    try {
      // Gửi yêu cầu kết bạn
      await axios.post(
        "http://localhost:8888/api/connect/friend/send-request", // URL của API gửi yêu cầu kết bạn
        formData, // Dữ liệu yêu cầu
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      console.log("Friend request sent successfully");
      return true; // Trả về true nếu gửi yêu cầu thành công
    } catch (err) {
      console.error("Error sending friend request:", err);
      return false; // Trả về false nếu có lỗi
    }
  };

  return sendRequest;
};

export default useFriendRequest;
