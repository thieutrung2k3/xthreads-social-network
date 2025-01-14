import React, { useEffect, useState } from "react";
import emailIcon from "./icon/email.png";
import addressIcon from "./icon/address-icon.png";
import dobIcon from "./icon/dob-icon.png";
import genderIcon from "./icon/gender-icon.png";
import phoneIcon from "./icon/phone-icon.png";
import editAvatar from "./icon/edit.png";
import {
  fetchMyUserInfo,
  fetchUserInfoByAccountID,
  getAccountIDFromToken,
  useTokenValidation,
  validateToken,
} from "./Utils/AuthUntil";
import Back from "./Back";
import { useParams } from "react-router-dom";
import axios from "axios";
import Posts from "./Post";
import EditProfile from "./EditProfile";
import useFriendRequest from "../service/friendService";

export default function Profile() {
  const token = localStorage.getItem("token");
  const { accountID } = useParams();
  console.log(accountID);
  const [myAccountID, setMyAccountID] = useState(null);

  useEffect(() => {
    const fetchAccountID = async () => {
      try {
        const id = await getAccountIDFromToken(token);
        setMyAccountID(id);
      } catch (error) {
        console.error("Error fetching account ID:", error.message);
      }
    };

    fetchAccountID();
  }, [token]);
  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState(null);
  const [isMe, setIsMe] = useState(false);
  const [posts, setPosts] = useState([]);
  const [uploadStatus, setUploadStatus] = useState("");

  const [showModal, setShowModal] = useState(false);

  const closeModal = () => setShowModal(false);

  const handleFileChange = async (event) => {
    const file = event.target.files[0];

    if (!file) {
      setUploadStatus("No file selected.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      setUploadStatus("Uploading...");

      const response = await axios.put(
        `http://localhost:8888/api/auth/account/update/avatar/${myAccountID}`, // Đường dẫn API backend
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${localStorage.getItem("token")}`, // Lấy token từ localStorage
          },
        }
      );

      setUploadStatus("Upload successful!");
      console.log(response.data);
      setTimeout(() => {
        window.location.reload(); // Reload trang
      }, 1000);
    } catch (error) {
      setUploadStatus(
        error.response?.data || "An error occurred during file upload."
      );
    }
  };

  useEffect(() => {
    const loadUserInfo = async () => {
      try {
        if (accountID === myAccountID) {
          console.log("is meeeeeeeeee");
          setIsMe(true);
          const data = await fetchMyUserInfo(token);
          setUserInfo(data);
        } else {
          const data = await fetchUserInfoByAccountID(accountID);
          setUserInfo(data);
        }

        const response = await axios.get(
          `http://localhost:8888/api/user-post/post/get/${accountID}`
        );
        setPosts(response.data.result || []);
      } catch (err) {
        setError(err.message);
      }
    };

    loadUserInfo();
  }, [accountID, myAccountID, token]);

  const openModal = () => {
    setShowModal(true);
  };
  const [requestSent, setRequestSent] = useState(false);
  const sendRequest = useFriendRequest();

  const handleSendFr = async () => {
    const result = await sendRequest(); // Gọi hàm gửi yêu cầu
    setRequestSent(result); // Lưu kết quả vào state
  };
  //friendddddddddddddddddddddddđ

  if (error) {
    return <p>Error: {error}</p>;
  }

  if (!userInfo) {
    return <p>Loading...</p>;
  }

  return (
    <div style={{ backgroundImage: "none" }}>
      <Back />
      <div className="main">
        <div className="left-profile">
          <div className="img-profile">
            <img
              src={`http://localhost:8888/api/user/image/${userInfo.urlProfilePicture}`}
              alt="Profile"
              onError={(e) => {
                e.target.src = "https://via.placeholder.com/150"; // Fallback image
              }}
              className="profile-img"
            />
            {isMe && (
              <div className="icon-container">
                <label htmlFor="upload-avatar" className="icon-edit-label">
                  <img className="icon-edit" src={editAvatar} alt="Edit" />
                </label>
                <input
                  id="upload-avatar"
                  type="file"
                  accept="image/*"
                  onChange={handleFileChange}
                  className="file-input"
                />
              </div>
            )}
          </div>
          <p className="name-user">
            {userInfo.lastName} {userInfo.firstName}
          </p>
          {!isMe && (
            <div className="button-friends">
              <button className="button-addfr" onClick={handleSendFr}>
                {requestSent ? "Pending" : "Add friend"}
              </button>
              <button className="button-msg">Message</button>
            </div>
          )}

          <div className="underline-user" />
          <div className="info-user">
            <div className="style-flex">
              <img className="icon" src={phoneIcon} alt="" />
              <p className="email-user">{userInfo.phone || "N/A"}</p>
            </div>
            <div className="style-flex">
              <img className="icon" src={emailIcon} alt="" />
              <p className="email-user">{userInfo.email}</p>
            </div>
            <div className="style-flex">
              <img className="icon" src={addressIcon} alt="" />
              <p className="email-user">{userInfo.address || "N/A"}</p>
            </div>
            <div className="style-flex">
              <img className="icon" src={dobIcon} alt="" />
              <p className="email-user">{userInfo.dob || "N/A"}</p>
            </div>
            <div className="style-flex">
              <img className="icon" src={genderIcon} alt="" />
              <p className="email-user">{userInfo.gender || "N/A"}</p>
            </div>
          </div>
          <div className="style-flex">
            {isMe && (
              <input
                type="button"
                value="Edit information"
                className="edit-infor-btn"
                onClick={openModal} // Xử lý sự kiện nếu cần
              />
            )}
            <EditProfile
              show={showModal}
              close={closeModal}
              userInfo={userInfo}
            />
          </div>
          <div className="underline-user" />
        </div>

        <div className="center">
          {posts.length > 0 ? (
            posts.map((post) => (
              <div key={post.postID}>
                <Posts post={post} />
              </div>
            ))
          ) : (
            <p>No posts available.</p>
          )}
        </div>
      </div>
    </div>
  );
}
