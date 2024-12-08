import React, { useEffect, useState } from "react";
import "./style.css";
import { fetchUserInfo, useTokenValidation } from "./Utils/AuthUntil.js";
import { useNavigate } from "react-router-dom";
import Profile from "./Profile.js";
import LogoutPage from "../service/authService.js";
export default function Home() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const toggleMenu = () => {
    setIsMenuOpen((prevState) => !prevState);
  };

  const [isProfileOpen, setIsProfileOpen] = useState(false);

  const toggleProfile = () => {
    setIsProfileOpen(() => true);
    setIsMenuOpen(() => false);
  };
  const toggleHome = () => {
    setIsProfileOpen(() => false);
  };
  const navigate = useNavigate();

  const performLogout = async () => {
    await LogoutPage(); // Gọi hàm logout
    navigate("/login"); // Điều hướng tới trang login sau khi logout thành công
  };
  useTokenValidation(() => {
    console.log("Token is valid.");
  });

  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadUserInfo = async () => {
      try {
        const data = await fetchUserInfo();
        setUserInfo(data);
      } catch (err) {
        setError(err.message);
      }
    };

    loadUserInfo();
  }, []);

  if (error) {
    return <p>Error: {error}</p>;
  }

  if (!userInfo) {
    return <p>Loading...</p>;
  }
  // Render giao diện
  return (
    <div
      style={{
        backgroundImage: "none",
      }}
    >
      <nav>
        <div className="left">
          <div className="logo">
            <img src="image/logo.png" alt="Logo" />
          </div>
          <div className="search_bar">
            <i className="fa-solid fa-magnifying-glass"></i>
            <input type="text" placeholder="Search" />
          </div>
        </div>

        <div className="center">
          <i className="fa-solid fa-house" onClick={toggleHome}></i>
          <i className="fa-solid fa-tv"></i>
          <i className="fa-solid fa-store"></i>
          <i className="fa-solid fa-users"></i>
        </div>

        <div className="right">
          <i className="fa-solid fa-list-ul"></i>
          <i className="fa-brands fa-facebook-messenger"></i>
          <i className="fa-solid fa-bell"></i>
          <i className="fa-solid fa-moon"></i>
          {/* My Avatar */}
          <img
            src={`http://localhost:8888/api/user/image/${userInfo.urlProfilePicture}`}
            alt="Profile"
            onError={(e) => {
              e.target.src = "https://via.placeholder.com/150"; // Chỉ dùng hình ảnh placeholder nếu không thể tải hình
            }}
            onClick={toggleMenu}
          />
          {isMenuOpen && ( // Hiển thị menu nếu `isMenuOpen` là true
            <ul className="dropDown">
              <li onClick={toggleProfile}>
                <p>Profile</p>
              </li>
              <li>
                <p>Settings</p>
              </li>
              <li>
                <p>Notifications</p>
              </li>
              <li onClick={performLogout}>
                <p>Logout</p>
              </li>
            </ul>
          )}
        </div>
      </nav>
      {isProfileOpen && <Profile userInfo={userInfo} />}
      {!isProfileOpen && (
        <div className="main">
          <div className="left">
            <div className="img">
              <img src="image/profile.png" alt="Profile" />
              <p>John Deo</p>
            </div>
            <div className="img">
              <img src="image/friend.png" alt="Friends" />
              <p>Friends</p>
            </div>
          </div>

          <div className="center">
            <div className="my_post">
              <div className="post_top">
                {/* My Avatar */}
                <img
                  src={`http://localhost:8888/api/user/image/${userInfo.urlProfilePicture}`}
                  alt="Profile"
                  onError={(e) => {
                    e.target.src = "https://via.placeholder.com/150"; // Hình ảnh placeholder nếu không tìm thấy ảnh
                  }}
                />
                <input
                  type="text"
                  placeholder={`What's on your mind, ${userInfo.firstName}?`}
                />
              </div>
              <hr />
              <div className="post_bottom">
                <div className="post_icon">
                  <i className="fa-solid fa-video red"></i>
                  <p>Live video</p>
                </div>
                <div className="post_icon">
                  <i className="fa-solid fa-images green"></i>
                  <p>Photo/video</p>
                </div>
                <div className="post_icon">
                  <i className="fa-regular fa-face-grin yellow"></i>
                  <p>Feeling/activity</p>
                </div>
              </div>
            </div>

            <div className="friends_post">
              <div className="friend_post_top">
                <div className="img_and_name">
                  <img src="image/profile_12.jpg" alt="Friend" />
                  <div className="friends_name">
                    <p className="friends_name">Senuda De Silva</p>
                    <p className="time">
                      16h. <i className="fa-solid fa-user-group"></i>
                    </p>
                  </div>
                </div>
                <div className="menu">
                  <i className="fa-solid fa-ellipsis"></i>
                </div>
              </div>
              <img src="image/post_5.jpg" alt="Post" />
              <div className="info">
                <div className="emoji_img">
                  <img src="image/like.png" alt="Like" />
                  <img src="image/haha.png" alt="Haha" />
                  <img src="image/heart.png" alt="Heart" />
                  <p>You, Charith Disanayaka and 25K others</p>
                </div>
                <div className="comment">
                  <p>421 Comments</p>
                  <p>1.3K Shares</p>
                </div>
              </div>
              <hr />
              <div className="like">
                <div className="like_icon">
                  <i className="fa-solid fa-thumbs-up activi"></i>
                  <p>Like</p>
                </div>
                <div className="like_icon">
                  <i className="fa-solid fa-message"></i>
                  <p>Comments</p>
                </div>
                <div className="like_icon">
                  <i className="fa-solid fa-share"></i>
                  <p>Share</p>
                </div>
              </div>
              <hr />
              <div className="comment_warpper">
                <img src="image/profile.png" alt="Profile" />
                <div className="circle"></div>
                <div className="comment_search">
                  <input type="text" placeholder="Write a comment" />
                  <i className="fa-regular fa-face-smile"></i>
                  <i className="fa-solid fa-camera"></i>
                  <i className="fa-regular fa-note-sticky"></i>
                </div>
              </div>
            </div>
            <div className="loard">
              <button>Load More</button>
            </div>
          </div>

          <div className="right">
            <div className="third_warpper">
              <div className="contact_tag">
                <h2>Contacts</h2>
                <div className="contact_icon">
                  <i className="fa-solid fa-video"></i>
                  <i className="fa-solid fa-magnifying-glass"></i>
                  <i className="fa-solid fa-ellipsis"></i>
                </div>
              </div>
              <div className="contact">
                <img src="image/profile_7.png" alt="Contact" />
                <p>Senuda De Silva</p>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
