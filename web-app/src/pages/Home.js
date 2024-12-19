import React, { useEffect, useState, useRef } from "react";
import "./style.css";
import {
  fetchMyUserInfo,
  getAccountIDFromToken,
  useTokenValidation,
} from "./Utils/AuthUntil.js";
import { useNavigate } from "react-router-dom";
import Profile from "./Profile.js";
import LogoutPage from "../service/authService.js";
import ChatApp from "./ChatApp.js";
import Back from "./Back.js";
import axios from "axios";
import Posts from "./Post.js";
import useInfiniteScroll from "./Utils/useInfiniteScroll.js";

export default function Home() {
  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState(null);

  useTokenValidation();

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

  //File
  //Create post
  const [formPost, setFormPost] = useState({
    accountID: "",
    content: "",
    status: "",
  });
  const fileInputRef = useRef(null); // Sử dụng ref để truy cập vào input file
  const [selectedFile, setSelectedFile] = useState(null); // State để lưu file đã chọn
  const [previewUrl, setPreviewUrl] = useState("");
  const handleFileSelect = (event) => {
    const file = event.target.files[0]; // Lấy file đầu tiên từ mảng files
    if (file) {
      setSelectedFile(file); // Gán file vào state
      setPreviewUrl(URL.createObjectURL(file));
      console.log("File selected:", file.name);
    }
  };
  useEffect(() => {
    return () => {
      if (previewUrl) {
        URL.revokeObjectURL(previewUrl);
      }
    };
  }, [previewUrl]);
  const handlePost = (e) => {
    const { name, value } = e.target;
    setFormPost({ ...formPost, [name]: value });
  };

  const removeImg = () => {
    if (previewUrl) {
      URL.revokeObjectURL(previewUrl); // Giải phóng URL
      setPreviewUrl(""); // Đặt lại previewUrl về rỗng
      setSelectedFile(null); // Xóa file đã chọn
    }
  };

  const createPost = async (e) => {
    e.preventDefault();

    // Tạo đối tượng FormData để gửi file và dữ liệu JSON
    const data = new FormData();
    data.append("file", selectedFile); // File được chọn
    data.append(
      "data",
      JSON.stringify({
        accountID: "formPost.status",
        content: formPost.content,
        status: "PUBLIC",
      })
    );

    // Lấy token từ nơi lưu trữ (ví dụ, localStorage)
    const token = localStorage.getItem("token"); // Hoặc dùng sessionStorage nếu bạn lưu ở đó

    try {
      const response = await axios.post(
        "http://localhost:8888/api/user-post/post/create",
        data,
        {
          headers: {
            "Content-Type": "multipart/form-data", // Chỉ định loại nội dung là FormData
            Authorization: `Bearer ${token}`, // Gửi token qua header Authorization
          },
        }
      );
      if (response.data.code === 1000) {
        alert("Post successful.");
        setFormPost({
          accountID: "",
          content: "",
          status: "",
        });
        if (previewUrl) {
          URL.revokeObjectURL(previewUrl); // Giải phóng URL
          setPreviewUrl(""); // Đặt lại previewUrl về rỗng
          setSelectedFile(null); // Xóa file đã chọn
        }
      } else {
        alert("Register failed.");
      }

      console.log("Post created:", response.data);
    } catch (error) {
      console.error("Error creating post:", error);
    }
  };

  const handleClickPhotoVideo = () => {
    // Mở input file khi người dùng click vào Photo/video
    fileInputRef.current.click();
  };
  const token = localStorage.getItem("token");
  const apiUrl = "http://localhost:8888/api/user-post/post/info";
  const { posts, loading, err } = useInfiniteScroll(0, 10, apiUrl, token);

  if (error) {
    return <p>Error: {error}</p>;
  }

  if (!userInfo) {
    return <p>Loading...</p>;
  }
  // Render giao diện
  return (
    <div>
      <Back />
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
                className="avt-post"
                src={`http://localhost:8888/api/user/image/${userInfo.urlProfilePicture}`}
                alt="Profile"
                onError={(e) => {
                  e.target.src = "https://via.placeholder.com/150"; // Hình ảnh placeholder nếu không tìm thấy ảnh
                }}
              />
              <input
                type="text"
                name="content"
                placeholder={`What's on your mind, ${userInfo.firstName}?`}
                value={formPost.content}
                onChange={handlePost}
              />
            </div>
            <hr />
            {previewUrl && (
              <div className="image-preview">
                <input type="submit" value="X" onClick={removeImg} />
                <img
                  src={previewUrl}
                  alt="Selected File"
                  style={{
                    maxWidth: "100%",
                    maxHeight: "300px",
                    marginTop: "5px",
                  }}
                />
              </div>
            )}
            <div className="post_bottom">
              <div className="post_icon" onClick={handleClickPhotoVideo}>
                <i className="fa-solid fa-images green"></i>
                <p>Photo/video</p>
                <input
                  type="file"
                  ref={fileInputRef}
                  style={{ display: "none" }} // Ẩn input file đi
                  onChange={handleFileSelect}
                />
              </div>
            </div>
            <hr />
            <input
              type="submit"
              value="Post"
              onClick={createPost}
              className="submit-post"
            />
          </div>
          {posts.length > 0 ? (
            posts.map((post) => (
              <div key={post.postID}>
                <Posts post={post} />
              </div>
            ))
          ) : (
            <p>No posts available.</p>
          )}
          {loading && <p>Loading more posts...</p>}
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
    </div>
  );
}
