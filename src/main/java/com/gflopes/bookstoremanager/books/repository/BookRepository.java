package com.gflopes.bookstoremanager.books.repository;

import com.gflopes.bookstoremanager.author.entity.Author;
import com.gflopes.bookstoremanager.books.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
