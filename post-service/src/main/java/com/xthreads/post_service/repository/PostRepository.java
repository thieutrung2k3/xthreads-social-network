package com.xthreads.post_service.repository;

import com.xthreads.post_service.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Posts,String> {
}
