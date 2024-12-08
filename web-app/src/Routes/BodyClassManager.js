import { useEffect } from "react";
import { useLocation } from "react-router-dom";

const BodyClassManager = () => {
  const location = useLocation();

  useEffect(() => {
    const currentPath = location.pathname;

    // Xóa tất cả các class hiện có của body
    document.body.className = "";

    // Gán class tương ứng với từng trang
    if (currentPath === "/") {
      document.body.classList.add("home-page");
    } else if (currentPath === "/login") {
      document.body.classList.add("login-page");
    } else if (currentPath === "/signup") {
      document.body.classList.add("signup-page");
    }
  }, [location.pathname]);

  return null; // Không render gì
};

export default BodyClassManager;