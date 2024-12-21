import React, { useEffect, useState } from "react";
import emailIcon from "./icon/email.png";
import addressIcon from "./icon/address-icon.png";
import dobIcon from "./icon/dob-icon.png";
import genderIcon from "./icon/gender-icon.png";
import phoneIcon from "./icon/phone-icon.png";
import {
  fetchMyUserInfo,
  fetchUserInfoByAccountID,
  getAccountIDFromToken,
} from "./Utils/AuthUntil";
import Back from "./Back";
import { useParams } from "react-router-dom";
import axios from "axios";
import Posts from "./Post";

export default function Profile() {
  const token = localStorage.getItem("token");
  const { accountID } = useParams();

  const myAccountID = getAccountIDFromToken(token);
  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState(null);
  const [isMe, setIsMe] = useState(false);
  const [posts, setPosts] = useState([]); // Đảm bảo posts là một mảng, không phải null

  useEffect(() => {
    const loadUserInfo = async () => {
      try {
        if (accountID === myAccountID) {
          console.log("is meeeeeeeeee");
          setIsMe(true);
          const data = await fetchMyUserInfo(token);
          setUserInfo(data);
        } else {
          const data = await fetchUserInfoByAccountID(accountID); // Fetch thông tin người dùng khác
          setUserInfo(data);
        }

        // Tải các bài viết của người dùng
        const response = await axios.get(
          `http://localhost:8888/api/user-post/post/get/${accountID}`
        );
        setPosts(response.data.result || []); // Đảm bảo posts là mảng
      } catch (err) {
        setError(err.message);
      }
    };

    loadUserInfo();
  }, [accountID, myAccountID, token]); // Dependency on accountID, myAccountID, and token

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
          </div>
          <p className="name-user">
            {userInfo.lastName} {userInfo.firstName}
          </p>

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
                onClick={() => alert("Edit information clicked")} // Xử lý sự kiện nếu cần
              />
            )}
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
