package com.vasylenkob.pastebin.repo;

import com.vasylenkob.pastebin.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
    Optional<User>findByEmail(String email);
    Optional<User>findByVerificationCode(String verificationCode);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
