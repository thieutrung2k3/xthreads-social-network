import axios from "axios";

// Hàm logout không dùng hook useNavigate trực tiếp trong đây
const logout = async () => {
  const token = localStorage.getItem("token");
  if (!token) return; // Nếu không có token, không làm gì cả

  try {
    // Gửi yêu cầu logout đến API
    await axios.post(
      "http://localhost:8888/api/auth/t/logout", // URL của API xác minh token
      { token }, // Gửi token trong body
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    // Xóa token khỏi localStorage
    localStorage.removeItem("token");
  } catch (err) {
    console.error("Logout failed:", err);
  }
};

export default logout;
