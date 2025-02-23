import React, { useState } from "react";
import axios from "axios";
import { getAccountIDFromToken } from "./Utils/AuthUntil";

const CommentInput = ({ postId, myUser }) => {
  const [commentText, setCommentText] = useState("");
  const accountId = getAccountIDFromToken(localStorage.getItem("token"));
  const handleCommentChange = (event) => {
    setCommentText(event.target.value);
  };

  const handleKeyDown = async (event) => {
    if (event.key === "Enter" && commentText.trim() !== "") {
      await addComment();
    }
  };

  const addComment = async () => {
    try {
      const token = localStorage.getItem("token");

      const response = await axios.post(
        "http://localhost:8888/api/user-post/comment/addComment",
        {
          postId: postId,
          commentText: commentText,
          accountId: accountId,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      console.log("Comment added successfully:", response.data);
      setCommentText(""); // Xóa nội dung input sau khi gửi
    } catch (error) {
      console.error("Lỗi khi thêm comment:", error);
    }
  };

  return (
    <div className="comment_wrapper">
      <img
        className="avt-post"
        src={`http://localhost:8888/api/user/image/${myUser.urlProfilePicture}`}
        alt="Profile"
        onError={(e) => {
          e.target.src = "https://via.placeholder.com/150"; // Hình placeholder nếu ảnh lỗi
        }}
      />
      <div className="circle"></div>
      <div className="comment_search">
        <input
          type="text"
          placeholder="Write a comment"
          value={commentText}
          onChange={handleCommentChange}
          onKeyDown={handleKeyDown} // Xử lý Enter để gửi
        />
        <i className="fa-regular fa-face-smile"></i>
        <i className="fa-solid fa-camera"></i>
        <i className="fa-regular fa-note-sticky"></i>
      </div>
    </div>
  );
};

export default CommentInput;
