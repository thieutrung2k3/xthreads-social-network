import React, { useState, useEffect } from "react";
import {
  fetchUserInfoByAccountID,
  getAccountIDFromToken,
} from "./Utils/AuthUntil";
import axios from "axios";

const FriendRequest = ({ request }) => {
  const [userInfo, setUserInfo] = useState(null);
  const [formData, setFormData] = useState({
    senderId: request.senderId,
    receiverId: request.receiverId,
  });
  useEffect(() => {
    const accountID = request.senderId;

    const fetchUserInfo = async () => {
      try {
        const data = await fetchUserInfoByAccountID(accountID);
        setUserInfo(data);
      } catch (error) {
        console.error("Error fetching user info:", error);
      }
    };

    fetchUserInfo(); // Call the fetch function
  }, [request.senderId]); // Dependency on senderId to refetch when it's changed

  const accept = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.put(
        `http://localhost:8888/api/connect/friend/accept-request`,
        formData, // Gửi request body
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      setTimeout(() => {
        window.location.reload(); // Reload trang
      }, 1000);

      console.log(response.data.message); // Hiển thị thông báo thành công
    } catch (error) {
      console.error("Error accepting friend request:", error);
    }
  };

  return (
    <div className="container-fr">
      {userInfo ? (
        <>
          <div className="left-content">
            <img
              className="avt-post"
              src={`http://localhost:8888/api/user/image/${userInfo.urlProfilePicture}`}
              alt="Profile"
              onError={(e) => {
                e.target.src = "https://via.placeholder.com/150"; // Hình ảnh placeholder nếu không tìm thấy ảnh
              }}
            />
            <p>
              {userInfo.lastName} {userInfo.firstName}
            </p>
          </div>
        </>
      ) : (
        <p>Loading user info...</p>
      )}
      <div className="button-container">
        <input
          type="button"
          value="Accept"
          className="accept-btn"
          onClick={accept}
        />
        <input type="button" value="Refuse" className="refuse-btn" />
      </div>
    </div>
  );
};

export default FriendRequest;
