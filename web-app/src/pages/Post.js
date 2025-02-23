import React, { useState, useEffect } from "react";
import {
  fetchMyUserInfo,
  fetchUserInfoByAccountID,
  getAccountIDFromToken,
} from "./Utils/AuthUntil"; // Assuming this function fetches user info
import { useNavigate } from "react-router-dom";
import axios from "axios";
import Comment from "./Comment";

const Posts = ({ post }) => {
  const navigate = useNavigate();
  const [userInfo, setUserInfo] = useState(null);
  const [myUser, setMyUser] = useState(null);
  const [comments, setComments] = useState([]);

  useEffect(() => {
    // Async function to fetch user info by account ID
    const fetchUserInfo = async () => {
      const accountID = post.accountID; // Assuming post contains accountID
      try {
        // Fetching user info and my info in parallel
        const [data, myData] = await Promise.all([
          fetchUserInfoByAccountID(accountID),
          fetchMyUserInfo(),
        ]);

        setUserInfo(data); // Set the fetched user info
        setMyUser(myData); // Set your user info
      } catch (error) {
        console.error("Error fetching user info:", error);
      }
    };

    fetchUserInfo(); // Call the fetch function
  }, [post.accountID]); // Run this effect when post.accountID changes// Dependency array: re-fetch if accountID changes

  const goToProfile = () => {
    if (userInfo && userInfo.accountID) {
      navigate(`/profile/${userInfo.accountID}`);
    }
  };

  const handleDelete = (id) => {
    setComments(comments.filter((comment) => comment.commentId !== id));
  };

  const getComment = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8888/api/user-post/comment/getAllPostComment/${post.postID}`
      );

      console.log("Dữ liệu nhận được:", response.data.result); //

      setComments(
        Array.isArray(response.data.result) ? response.data.result : []
      );
    } catch (error) {
      console.error("Lỗi khi lấy comment:", error);
    }
  };

  const [commentText, setCommentText] = useState("");
  const accountId = getAccountIDFromToken(localStorage.getItem("token"));
  const handleCommentChange = (event) => {
    setCommentText(event.target.value);
  };

  const handleKeyDown = async (event) => {
    if (event.key === "Enter" && commentText.trim() !== "") {
      await addComment();
    }
    getComment();
  };

  const addComment = async () => {
    try {
      const token = localStorage.getItem("token");

      const response = await axios.post(
        "http://localhost:8888/api/user-post/comment/addComment",
        {
          postId: post.postID,
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

  const addReaction = async () => {
    try {
      const token = localStorage.getItem("token");

      const response = await axios.post(
        "http://localhost:8888/api/user-post/reaction",
        {
          postId: post.postID,
          reactionType: "LIKE",
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

  if (!userInfo) {
    return <p>Loading...</p>;
  }

  return (
    <>
      <div className="friends_post">
        <div className="friend_post_top">
          <div className="img_and_name">
            <img
              className="avt-post"
              src={`http://localhost:8888/api/user/image/${userInfo.urlProfilePicture}`}
              alt="Profile"
              onError={(e) => {
                e.target.src = "https://via.placeholder.com/150"; // Hình ảnh placeholder nếu không tìm thấy ảnh
              }}
              onClick={goToProfile}
            />
            <div className="friends_name">
              <p className="friends_name" onClick={goToProfile}>
                {userInfo.lastName} {userInfo.firstName}{" "}
                {/* Display user info */}
              </p>
              <p className="time">
                16h. <i className="fa-solid fa-user-group"></i>
              </p>
            </div>
          </div>
          <div className="menu">
            <i className="fa-solid fa-ellipsis"></i>
          </div>
        </div>
        <p>{post.content}</p>
        {post.imageUrl && (
          <img
            src={`http://localhost:8888/api/user-post/image/${post.imageUrl}`}
            alt="Post"
          />
        )}

        <div className="info">
          <div className="comment">
            <p>0 Comments</p>
            <p>0 Shares</p>
          </div>
        </div>
        <hr />

        <div className="like">
          <div className="like_icon" onClick={addReaction}>
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
        <p onClick={getComment}>View comment</p>
        {comments.map((comment) => (
          <div className="app">
            <Comment
              accountId={comment.accountId}
              commentText={comment.commentText}
              timeStamp={comment.timestamp}
              commentId={comment.commentId}
              getComment={handleDelete}
            />
          </div>
        ))}
        <div className="comment_warpper">
          <img
            className="avt-post"
            src={`http://localhost:8888/api/user/image/${myUser.urlProfilePicture}`}
            alt="Profile"
            onError={(e) => {
              e.target.src = "https://via.placeholder.com/150"; // Hình ảnh placeholder nếu không tìm thấy ảnh
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
      </div>
    </>
  );
};

export default Posts;
