package com.gflopes.bookstoremanager.book.repository;

import com.gflopes.bookstoremanager.book.entity.Book;
import com.gflopes.bookstoremanager.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByNameAndIsbnAndUser(String name, String isbn, User user);

     Optional<Book> findByIdAndUser(Long Long, User user);

     List<Book> findAllByUser(User user);
}
