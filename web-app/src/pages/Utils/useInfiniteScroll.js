import { useState, useEffect } from "react";
import axios from "axios";

const useInfiniteScroll = (initialPage = 0, pageSize = 10, apiUrl, token) => {
  const [posts, setPosts] = useState([]); // Danh sách bài đăng
  const [page, setPage] = useState(initialPage); // Số trang hiện tại
  const [totalPages, setTotalPages] = useState(0); // Tổng số trang
  const [loading, setLoading] = useState(false); // Trạng thái tải dữ liệu
  const [error, setError] = useState(null); // Trạng thái lỗi khi tải dữ liệu

  // Hàm load bài đăng
  const loadPosts = async (pageNumber = 0) => {
    setLoading(true);
    try {
      const response = await axios.get(
        `${apiUrl}?page=${pageNumber}&size=${pageSize}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      const { content, totalPages } = response.data.result;
      setPosts((prevPosts) => [...prevPosts, ...content]); // Thêm bài viết mới vào danh sách hiện có
      setTotalPages(totalPages); // Cập nhật tổng số trang
    } catch (error) {
      setError(error);
      console.error("Error loading posts:", error);
    } finally {
      setLoading(false);
    }
  };

  // Lắng nghe sự kiện cuộn để tải thêm bài viết
  const handleScroll = () => {
    // Kiểm tra khi người dùng cuộn đến đáy trang
    const bottom =
      window.innerHeight + document.documentElement.scrollTop >=
      document.documentElement.scrollHeight;

    // Kiểm tra nếu cuộn đến cuối trang và chưa đạt tối đa số trang
    if (bottom && !loading && page < totalPages - 1) {
      setPage((prevPage) => prevPage + 1); // Tăng số trang khi cuộn đến cuối
    }
  };

  // Gọi loadPosts mỗi khi page thay đổi
  useEffect(() => {
    loadPosts(page);
  }, [page]);

  // Lắng nghe sự kiện cuộn
  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, [loading, page, totalPages]);

  return {
    posts,
    loading,
    error,
  };
};

export default useInfiniteScroll;
