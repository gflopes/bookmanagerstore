package com.gflopes.bookstoremanager.author.service;

import com.gflopes.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.gflopes.bookstoremanager.author.mapper.AuthorMapper;
import com.gflopes.bookstoremanager.author.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    private final AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private AuthorDTOBuilder authorDtoBuilder;

    @BeforeEach
    void setUp() {
        authorDtoBuilder = AuthorDTOBuilder.builder().build();
    }

}