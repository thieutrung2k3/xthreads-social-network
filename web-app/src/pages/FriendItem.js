import React, { useState, useEffect } from "react";
import "./css/Register.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { fetchUserInfoByAccountID } from "./Utils/AuthUntil"; // Nếu đây là hàm bất đồng bộ

const FriendItem = ({ id }) => {
  const [userInfo, setUserInfo] = useState(null);
  const navigate = useNavigate();
  useEffect(() => {
    const fetchUserData = async () => {
      const data = await fetchUserInfoByAccountID(id); // Giả sử hàm này trả về thông tin người dùng
      setUserInfo(data); // Cập nhật dữ liệu người dùng vào state
    };

    if (id) {
      fetchUserData(); // Gọi hàm lấy thông tin khi id có giá trị
    }
  }, [id]); // Dependency array: hàm này chỉ chạy lại khi id thay đổi
  const goToProfile = () => {
    if (userInfo && userInfo.accountID) {
      navigate(`/profile/${userInfo.accountID}`);
    }
  };
  if (!userInfo) {
    return <p>Loading...</p>; // Hiển thị khi dữ liệu chưa được tải
  }

  return (
    <div className="contact" onClick={goToProfile}>
      <img
        src={`http://localhost:8888/api/user/image/${userInfo.urlProfilePicture}`}
        alt="Contact"
      />
      <p>
        {userInfo.lastName} {userInfo.firstName}
      </p>
    </div>
  );
};

export default FriendItem;
