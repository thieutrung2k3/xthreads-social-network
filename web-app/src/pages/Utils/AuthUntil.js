import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";

/**
 * Hàm kiểm tra token và điều hướng nếu không hợp lệ.
 * @param {Function} onSuccess - Hàm callback gọi khi token hợp lệ.
 */
export const useTokenValidation = () => {
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

        if (response.data.code === 1000) {
          if (response.data.result.valid === false) {
            localStorage.removeItem("token");
            navigate("/login");
          }
        }
      } catch (error) {
        alert("Token validation failed: " + error);
        localStorage.removeItem("token");
        navigate("/login");
      }
    };

    checkToken();
  }, [navigate]);
};

export const validateToken = async ({ token }) => {
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

    if (response.data.code === 1000) {
      return response.data.result.valid;
    }

    return false;
  } catch (error) {
    // Xử lý lỗi, nếu có lỗi xảy ra trong quá trình gọi API
    console.error("Error validating token:", error);
    return false;
  }
};

export const getAccountIDFromToken = (token) => {
  if (!token) return null;
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

export const fetchMyUserInfo = async () => {
  const token = localStorage.getItem("token"); // Lấy token từ localStorage
  if (!token) return null;

  const accountID = getAccountIDFromToken(token); // Giải mã username từ token
  if (!accountID) throw new Error("Invalid token: no username found");

  try {
    const response = await axios.get(
      `http://localhost:8888/api/user/information/get/${accountID}`, // Endpoint lấy thông tin
      {
        headers: {
          Authorization: `Bearer ${token}`, // Gửi token trong Authorization header
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

export const fetchUserInfoByAccountID = async (accountID) => {
  if (!accountID) {
    throw new Error("No account ID provided");
  }

  try {
    const response = await axios.get(
      `http://localhost:8888/api/user/information/get/${accountID}` // Endpoint to get user information
    );

    const { data } = response;

    if (response.status !== 200) {
      throw new Error(
        `Failed to fetch user info. Status code: ${response.status}`
      );
    }

    if (data.code === 1000 && data.result) {
      return data.result;
    } else {
      throw new Error(data.message || "Failed to fetch user information");
    }
  } catch (error) {
    if (axios.isAxiosError(error)) {
      console.error("Axios Error:", error.message);
    } else {
      console.error("General Error:", error.message);
    }
    throw error;
  }
};
