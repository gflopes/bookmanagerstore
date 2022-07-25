package com.gflopes.bookstoremanager.book.service;

import com.gflopes.bookstoremanager.author.entity.Author;
import com.gflopes.bookstoremanager.author.service.AuthorService;
import com.gflopes.bookstoremanager.book.dto.BookRequestDTO;
import com.gflopes.bookstoremanager.book.dto.BookResponseDTO;
import com.gflopes.bookstoremanager.book.entity.Book;
import com.gflopes.bookstoremanager.book.exception.BookAlreadyExistsException;
import com.gflopes.bookstoremanager.book.exception.BookNotFoundException;
import com.gflopes.bookstoremanager.book.mapper.BookMapper;
import com.gflopes.bookstoremanager.book.repository.BookRepository;
import com.gflopes.bookstoremanager.publisher.entity.Publisher;
import com.gflopes.bookstoremanager.publisher.service.PublisherService;
import com.gflopes.bookstoremanager.user.dto.AuthenticatedUser;
import com.gflopes.bookstoremanager.user.entity.User;
import com.gflopes.bookstoremanager.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookService {

    private final static BookMapper bookMapper = BookMapper.INSTANCE;

    private UserService userService;

    private PublisherService publisherService;

    private AuthorService authorService;

    private BookRepository bookRepository;

    public BookResponseDTO create(AuthenticatedUser authenticatedUser, BookRequestDTO bookRequestDTO) {
        User foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        verifyIfBookIsAlreadyRegistered(foundAuthenticatedUser, bookRequestDTO);

        Author foundAuthor = authorService.verifyAndGetAuthorIfExists(bookRequestDTO.getAuthorId());
        Publisher foundPublisher = publisherService.verifyAndGetPublisherIfExists(bookRequestDTO.getPublisherId());

        Book bookToCreate = bookMapper.toModel(bookRequestDTO);
        bookToCreate.setUser(foundAuthenticatedUser);
        bookToCreate.setAuthor(foundAuthor);
        bookToCreate.setPublisher(foundPublisher);
        Book bookCreated = bookRepository.save(bookToCreate);

        return bookMapper.toDTO(bookCreated);
    }

    public BookResponseDTO findByIdAndUser(AuthenticatedUser authenticatedUser, Long bookId) {
        User foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());

        return bookRepository.findByIdAndUser(bookId, foundAuthenticatedUser)
                .map(bookMapper::toDTO)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    public List<BookResponseDTO> findAllByUser(AuthenticatedUser authenticatedUser) {
        User foundAuthenticatedUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());

        return bookRepository.findAllByUser(foundAuthenticatedUser)
                .stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void verifyIfBookIsAlreadyRegistered(User foundUser, BookRequestDTO bookRequestDTO) {
        bookRepository.findByNameAndIsbnAndUser(bookRequestDTO.getName(), bookRequestDTO.getIsbn(), foundUser)
                .ifPresent(duplicateBook -> {
                    throw new BookAlreadyExistsException(bookRequestDTO.getName(), bookRequestDTO.getIsbn(), foundUser.getUsername());
                });
    }

}
