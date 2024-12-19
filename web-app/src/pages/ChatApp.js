import React, { useState, useEffect } from "react";
import axios from "axios";

const ChatApp = () => {
  const [userData, setUserData] = useState([]); // Khởi tạo userData là mảng rỗng
  const [error, setError] = useState(null); // Quản lý lỗi
  const [formData, setFormData] = useState({}); // Quản lý dữ liệu form (nếu có)

  const handleSubmit = async (e) => {
    e.preventDefault(); // Ngăn form reload lại trang
    try {
      // Gửi yêu cầu đăng nhập tới API
      const response = await axios.get(
        "http://localhost:8888/api/user/information/get-all-users",
        formData,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      const data = response.data;
      if (data.code === 1000) {
        setUserData(data.result); // Cập nhật userData
      } else {
        setError("Invalid response code: " + data.code);
      }
    } catch (error) {
      if (error.response) {
        setError(error.response.data.message || "Failed.");
      } else {
        setError("Something went wrong. Please try again.");
      }
    }
  };
  return (
    <div>
      <div className="main">
        <div className="left-msg">
          <input type="button" onClick={handleSubmit} />
          {userData.length > 0 ? (
            userData.map((user) => (
              <div className="title-msg" key={user.id}>
                <img
                  src={`http://localhost:8888/api/user/image/${user.urlProfilePicture}`}
                  alt="Profile"
                  onError={(e) => {
                    e.target.src = "https://via.placeholder.com/150"; // Hình ảnh placeholder nếu không tìm thấy ảnh
                  }}
                />
                <p>
                  {user.lastName} {user.firstName}
                </p>
              </div>
            ))
          ) : (
            <p>No users available</p> // Thông báo nếu danh sách trống
          )}
        </div>
      </div>
    </div>
  );
};

export default ChatApp;
