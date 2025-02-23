import React, { useEffect, useState } from "react";
import { fetchUserInfoByAccountID } from "./Utils/AuthUntil";
import axios from "axios";

const Comment = ({
  accountId,
  commentText,
  timestamp,
  commentId,
  getComment,
}) => {
  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadUserInfo = async () => {
      try {
        console.log("Fetching user for accountId:", accountId);
        const data = await fetchUserInfoByAccountID(accountId);
        console.log("Fetched user data:", data);
        setUserInfo(data);
      } catch (err) {
        console.error("Fetch error:", err);
        setError(err.message);
      }
    };

    if (accountId) {
      loadUserInfo();
    }
  }, [accountId]);

  const deleteComment = async () => {
    try {
      const token = localStorage.getItem("token");

      const response = await axios.delete(
        `http://localhost:8888/api/user-post/comment/deleteComment/${commentId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      getComment(commentId);
    } catch (error) {
      console.error("Lỗi khi thêm comment:", error);
    }
  };
  return (
    <div className="comment">
      <div className="comment-content">
        {error ? (
          <p className="error">⚠️ {error}</p>
        ) : userInfo ? (
          <>
            <div className="comment_warpper">
              <img
                className="avt-post"
                src={`http://localhost:8888/api/user/image/${userInfo.urlProfilePicture}`}
                alt="Profile"
                onError={(e) => {
                  e.target.src = "https://via.placeholder.com/150"; // Hình ảnh placeholder nếu không tìm thấy ảnh
                }}
              />
              <div>
                {" "}
                <p className="name">
                  {userInfo.lastName} {userInfo.firstName}
                </p>
                <p className="text">{commentText}</p>
                <div className="actions">
                  <span>{timestamp}</span>
                  <span>Like</span>
                  <span>Reply</span>
                  <span>Share</span>
                  <span onClick={deleteComment}>Delete</span>
                </div>
              </div>
            </div>
          </>
        ) : (
          <p>Loading...</p>
        )}
      </div>
    </div>
  );
};

export default Comment;
