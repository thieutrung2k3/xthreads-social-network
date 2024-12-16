import React from "react";
import { useLocation } from "react-router-dom";

const Background = () => {
  const location = useLocation();

  // Kiểm tra nếu route hiện tại thuộc danh sách cần hiển thị background
  const routesWithBackground = ["/login", "/signup"];
  const isBackgroundVisible = routesWithBackground.includes(location.pathname);

  return (
    <div
      style={{
        position: "fixed",
        top: 0,
        left: 0,
        width: "100%",
        height: "100%",
        backgroundImage: isBackgroundVisible
          ? "url('https://via.placeholder.com/1920x1080')" // URL ảnh nền
          : "none", // Không hiển thị background
        backgroundSize: "cover",
        backgroundPosition: "center",
        zIndex: -1,
        transition: "background-image 0.5s ease",
      }}
    ></div>
  );
};

export default Background;
