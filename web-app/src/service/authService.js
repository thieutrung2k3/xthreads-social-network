import axios from "axios";

const logout = async () => {
  const token = localStorage.getItem("token");
  console.log(token);
  if (!token) return;

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
    localStorage.removeItem("token");
    console.error("Logout failed:", err);
  }
};

export default logout;
