package com.xthreads.auth_service.repository;

import com.xthreads.auth_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByUsername(String username);
    Optional<Account> findByUsername(String username);
}
