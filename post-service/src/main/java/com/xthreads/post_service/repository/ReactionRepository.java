package com.xthreads.post_service.repository;

import com.xthreads.post_service.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, String> {
    List<Reaction> findAllByPostId(String postId);
    Optional<Reaction> findByAccountIdAndPostId(String accountId, String postId);
}
