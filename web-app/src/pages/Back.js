import React, { useEffect, useState } from "react";
import "./style.css";
import { useNavigate } from "react-router-dom";
import LogoutPage from "../service/authService.js";
import { fetchMyUserInfo, getAccountIDFromToken } from "./Utils/AuthUntil.js";
import axios from "axios";
import FriendRequest from "./FriendRequest.js";

export default function Back() {
  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  // States for dropdown and other toggles
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isNotifyOpen, setIsNotifyOpen] = useState(false);

  // Load user information dynamically
  useEffect(() => {
    const loadUserInfo = async () => {
      try {
        const data = await fetchMyUserInfo();
        setUserInfo(data);
      } catch (err) {
        setError(err.message);
      }
    };

    loadUserInfo();
  }, []);

  const toggleMenu = () => setIsMenuOpen((prevState) => !prevState);

  const [requests, setRequests] = useState(null);
  const toggleNotify = () => {
    setIsNotifyOpen((prevState) => !prevState);

    // Only make the API call if the notification is opening
    if (!isNotifyOpen) {
      const token = localStorage.getItem("token");
      const accountID = getAccountIDFromToken(token);

      const loadNotification = async () => {
        try {
          // Ensure the token is passed in the Authorization header
          const response = await axios.get(
            `http://localhost:8888/api/connect/friend/pending-requests`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
              params: {
                receiverId: accountID, // Ensure this matches the @RequestParam
              },
            }
          );

          setRequests(response.data.result);
          console.log(response.data.message);
        } catch (error) {
          console.error("Error fetching notifications:", error);
        }
      };

      loadNotification();
    } else {
      setRequests(null);
    }
  };

  const performLogout = async () => {
    await LogoutPage(); // Logout user
    navigate("/login");
  };

  // Handle dynamic navigation to user profile
  const goToProfile = () => {
    if (userInfo && userInfo.accountID) {
      navigate(`/profile/${userInfo.accountID}`);
    }
    setIsMenuOpen(false);
  };

  const goToChangePassword = () => {
    navigate(`/changePassword`);
  };

  const goToHome = () => navigate("/");

  if (error) return <p>Error: {error}</p>;
  if (!userInfo) return;

  return (
    <>
      <nav>
        <div className="left">
          {/* <div className="logo">
            <img src="image/logo.png" alt="Logo" />
          </div> */}
          <div className="search_bar">
            <i className="fa-solid fa-magnifying-glass"></i>
            <input type="text" placeholder="Search" />
          </div>
        </div>

        <div className="center">
          <i className="fa-solid fa-house" onClick={goToHome}></i>
          <i className="fa-solid fa-tv"></i>
          <i className="fa-solid fa-store"></i>
          <i className="fa-solid fa-users"></i>
        </div>

        <div className="right">
          <i className="fa-solid fa-list-ul"></i>
          <i className="fa-brands fa-facebook-messenger"></i>
          <i className="fa-solid fa-bell" onClick={toggleNotify}></i>
          {isNotifyOpen && (
            <ul className="dropDown-f">
              {requests ? (
                requests.length > 0 ? (
                  requests.map((request) => (
                    <li key={request.id}>
                      <FriendRequest request={request} />
                    </li>
                  ))
                ) : (
                  <p>No notifications available.</p>
                )
              ) : (
                <p>Loading notifications...</p>
              )}
            </ul>
          )}

          <i className="fa-solid fa-moon"></i>

          {/* User Avatar */}
          <img
            src={`http://localhost:8888/api/user/image/${userInfo.urlProfilePicture}`}
            alt="Profile"
            onError={(e) => (e.target.src = "https://via.placeholder.com/150")}
            onClick={toggleMenu}
          />

          {/* Dropdown Menu */}
          {isMenuOpen && (
            <ul className="dropDown">
              <li onClick={goToProfile} className="li-first">
                <img
                  src={`http://localhost:8888/api/user/image/${userInfo.urlProfilePicture}`}
                  alt="Profile"
                  onError={(e) =>
                    (e.target.src = "https://via.placeholder.com/150")
                  }
                />
                <p>
                  {userInfo.lastName} {userInfo.firstName}
                </p>
              </li>
              <li>
                <p>Settings</p>
              </li>
              <li onClick={goToChangePassword}>
                <p>Change password</p>
              </li>
              <li onClick={performLogout}>
                <p>Logout</p>
              </li>
            </ul>
          )}
        </div>
      </nav>
    </>
  );
}
