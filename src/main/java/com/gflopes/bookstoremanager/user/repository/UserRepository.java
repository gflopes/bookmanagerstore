package com.gflopes.bookstoremanager.user.repository;

import com.gflopes.bookstoremanager.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmailOrUsername(String email, String username);
}
