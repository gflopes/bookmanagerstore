package com.gflopes.bookstoremanager.users.repository;

import com.gflopes.bookstoremanager.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
