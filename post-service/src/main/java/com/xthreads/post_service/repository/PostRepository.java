package com.xthreads.post_service.repository;

import com.xthreads.post_service.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    Optional<List<Post>> findAllByAccountID(String accountID);
}
