import React, { useState } from "react";
import "./Popup.css";

const Menu = () => {
  const [isPopupVisible, setIsPopupVisible] = useState(false);

  // Hàm hiển thị popup khi nhấn vào nút
  const togglePopup = () => {
    setIsPopupVisible(!isPopupVisible);
  };

  return (
    <div className="menu">
      <div className="menu-item">
        <i className="fas fa-cog"></i>
        <span>Cài đặt & quyền riêng tư</span>
        <i className="fas fa-chevron-right arrow"></i>
      </div>
      <div className="menu-item">
        <i className="fas fa-question-circle"></i>
        <span>Trợ giúp & hỗ trợ</span>
        <i className="fas fa-chevron-right arrow"></i>
      </div>
      <div className="menu-item">
        <i className="fas fa-moon"></i>
        <span>Màn hình & trợ năng</span>
        <i className="fas fa-chevron-right arrow"></i>
      </div>
      <div className="menu-item">
        <i className="fas fa-comment-dots"></i>
        <span>Đóng góp ý kiến</span>
        <span className="shortcut">CTRL B</span>
      </div>
      <div className="menu-item" onClick={togglePopup}>
        <i className="fas fa-sign-out-alt"></i>
        <span>Đăng xuất</span>
      </div>

      {/* Popup */}
      <div className={`popup ${isPopupVisible ? "show" : ""}`}>
        Đây là popup hiển thị ngay dưới nút.
      </div>

      <div className="footer">
        <a href="#">Quyền riêng tư</a> ·<a href="#">Điều khoản</a> ·
        <a href="#">Quảng cáo</a> ·<a href="#">Lựa chọn quảng cáo</a>
        <i className="fas fa-ad"></i> ·<a href="#">Cookie</a> ·
        <a href="#">Xem thêm</a>
        <br />
        Meta © 2024
      </div>
    </div>
  );
};

export default Menu;
