import axios from "axios";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function Home() {
  const navigate = useNavigate();

  useEffect(() => {
    const checkToken = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        // Nếu không có token, điều hướng tới trang login
        navigate("/login");
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
          navigate("/login");
        }
      } catch (error) {
        // Xử lý khi gọi API thất bại
        alert("Token validation failed:" + error);
        localStorage.removeItem("token");
        navigate("/login");
      }
    };

    checkToken(); // Gọi hàm kiểm tra token khi component được render
  }, [navigate]);
  return (
    <>
      <h2>This is home.</h2>
    </>
  );
}
