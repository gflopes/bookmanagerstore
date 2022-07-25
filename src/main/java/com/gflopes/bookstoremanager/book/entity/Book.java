package com.gflopes.bookstoremanager.book.entity;

import com.gflopes.bookstoremanager.author.entity.Author;
import com.gflopes.bookstoremanager.entity.Auditable;
import com.gflopes.bookstoremanager.publisher.entity.Publisher;
import com.gflopes.bookstoremanager.user.entity.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Book extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String isbn;

    @Column(columnDefinition = "integer default 0")
    private Integer pages;

    @Column(columnDefinition = "integer default 0")
    private Integer chapters;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Author author;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Publisher publisher;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;
}
