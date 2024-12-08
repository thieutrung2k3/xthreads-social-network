import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";

/**
 * Hàm kiểm tra token và điều hướng nếu không hợp lệ.
 * @param {Function} onSuccess - Hàm callback gọi khi token hợp lệ.
 */
export const useTokenValidation = (onSuccess) => {
  const navigate = useNavigate();

  useEffect(() => {
    const checkToken = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        navigate("/login");
        return;
      }

      try {
        const response = await axios.post(
          "http://localhost:8888/api/auth/t/validate",
          { token },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (
          response.data.code !== 1000 ||
          response.data.result.result === false
        ) {
          localStorage.removeItem("token");
          navigate("/login");
        } else {
          // Token hợp lệ, gọi hàm onSuccess
          if (onSuccess) onSuccess();
        }
      } catch (error) {
        alert("Token validation failed: " + error);
        localStorage.removeItem("token");
        navigate("/login");
      }
    };

    checkToken();
  }, [navigate, onSuccess]);
};

export const getUserInfoFromToken = (token) => {
  if (!token) throw new Error("Token is required.");
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split("")
        .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
        .join("")
    );
    const payload = JSON.parse(jsonPayload);
    if (!payload) return null;

    return payload.sub;
  } catch (error) {
    throw new Error("Invalid token format.");
  }
};

export const fetchUserInfo = async () => {
  const token = localStorage.getItem("token"); // Lấy token từ localStorage
  if (!token) throw new Error("No token found");

  const username = getUserInfoFromToken(token); // Giải mã username từ token
  if (!username) throw new Error("Invalid token: no username found");

  try {
    const response = await axios.get(
      `http://localhost:8888/api/user/information/get/${username}`, // Endpoint lấy thông tin
      {
        headers: {
          Authorization: `Bearer: ${token}`, // Gửi token trong Authorization header
          "Content-Type": "application/json",
        },
      }
    );

    const { data } = response;
    if (data.code === 1000 && data.result) {
      return data.result; // Trả về thông tin người dùng
    } else {
      throw new Error("Failed to fetch user information");
    }
  } catch (error) {
    console.error("Error fetching user info:", error);
    throw error;
  }
};