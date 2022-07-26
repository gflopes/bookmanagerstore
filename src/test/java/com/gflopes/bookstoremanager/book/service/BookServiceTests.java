package com.gflopes.bookstoremanager.book.service;

import com.gflopes.bookstoremanager.author.entity.Author;
import com.gflopes.bookstoremanager.author.service.AuthorService;
import com.gflopes.bookstoremanager.book.builder.BookRequestDTOBuilder;
import com.gflopes.bookstoremanager.book.builder.BookResponseDTOBuilder;
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
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    private final BookMapper bookMapper = BookMapper.INSTANCE;

    @Mock
    private UserService userService;

    @Mock
    private PublisherService publisherService;

    @Mock
    private AuthorService authorService;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private BookRequestDTOBuilder bookRequestDTOBuilder;

    private BookResponseDTOBuilder bookResponseDTOBuilder;

    private AuthenticatedUser authenticatedUser;

    @BeforeEach
    void setUp() {
        bookRequestDTOBuilder = BookRequestDTOBuilder.builder().build();
        bookResponseDTOBuilder = BookResponseDTOBuilder.builder().build();
        authenticatedUser = new AuthenticatedUser("rodrigo", "123456", "ADMIN");
    }

    @Test
    void whenNewBookIsInformedThenShouldBeCreated() {
        BookRequestDTO expectedBookRequestDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        BookResponseDTO expectedBookResponseDTO = bookResponseDTOBuilder.buildResponseBookDTO();
        Book expectedCreatedBook = bookMapper.toModel(expectedBookResponseDTO);

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByNameAndIsbnAndUser(
                eq(expectedBookRequestDTO.getName()),
                eq(expectedBookRequestDTO.getIsbn()),
                any(User.class))).thenReturn(Optional.empty());
        when(authorService.verifyAndGetAuthorIfExists(expectedBookRequestDTO.getAuthorId())).thenReturn(new Author());
        when(publisherService.verifyAndGetPublisherIfExists(expectedBookRequestDTO.getPublisherId())).thenReturn(new Publisher());
        when(bookRepository.save(any(Book.class))).thenReturn(expectedCreatedBook);

        BookResponseDTO createdBookResponseDTO = bookService.create(authenticatedUser, expectedBookRequestDTO);
        assertThat(createdBookResponseDTO, Matchers.is(equalTo(expectedBookResponseDTO)));
    }

    @Test
    void whenExistingBookIsInformedToCreateThenAnExceptionShouldBeThrown() {
        BookRequestDTO expectedBookRequestDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        BookResponseDTO expectedBookResponseDTO = bookResponseDTOBuilder.buildResponseBookDTO();
        Book expectedDuplicatedBook = bookMapper.toModel(expectedBookResponseDTO);

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByNameAndIsbnAndUser(
                eq(expectedBookRequestDTO.getName()),
                eq(expectedBookRequestDTO.getIsbn()),
                any(User.class))).thenReturn(Optional.of(expectedDuplicatedBook));

        assertThrows(BookAlreadyExistsException.class, () -> bookService.create(authenticatedUser, expectedBookRequestDTO));
    }

    @Test
    void whenExistingBookIsInformedThenABookExceptionShouldBeReturned() {
        BookRequestDTO expectedBookFindDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        BookResponseDTO expectedBookFoundDTO = bookResponseDTOBuilder.buildResponseBookDTO();
        Book expectedFoundBook = bookMapper.toModel(expectedBookFoundDTO);

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(eq(expectedBookFindDTO.getId()), any(User.class))).thenReturn(Optional.of(expectedFoundBook));

        BookResponseDTO foundBookDTO = bookService.findByIdAndUser(authenticatedUser, expectedBookFindDTO.getId());
        assertThat(foundBookDTO, Matchers.is(equalTo(expectedBookFoundDTO)));
    }

    @Test
    void whenNotExistingBookIsInformedThenABookExceptionShouldBeThrown() {
        BookRequestDTO expectedBookFindDTO = bookRequestDTOBuilder.buildRequestBookDTO();

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(eq(expectedBookFindDTO.getId()), any(User.class))).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.findByIdAndUser(authenticatedUser, expectedBookFindDTO.getId()));
    }

    @Test
    void whenListBookIsCalledThenItShouldByReturned() {
        BookResponseDTO expectedBookFoundDTO = bookResponseDTOBuilder.buildResponseBookDTO();
        Book expectedFoundBook = bookMapper.toModel(expectedBookFoundDTO);

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findAllByUser(any(User.class))).thenReturn(Collections.singletonList(expectedFoundBook));

        List<BookResponseDTO> returnedBookResponseList = bookService.findAllByUser(authenticatedUser);
        assertThat(returnedBookResponseList.size(), is(1));
        assertThat(returnedBookResponseList.get(0), is(equalTo(expectedBookFoundDTO)));
    }

    @Test
    void whenListBookIsCalledThenEmptyListItShouldByReturned() {
        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findAllByUser(any(User.class))).thenReturn(Collections.emptyList());

        List<BookResponseDTO> returnedBookResponseList = bookService.findAllByUser(authenticatedUser);
        assertThat(returnedBookResponseList.size(), is(0));
    }

    @Test
    void whenExistingBookIdIsInformedThenItShouldBeDeleted() {
        BookResponseDTO expectedBookToDeleteDTO = bookResponseDTOBuilder.buildResponseBookDTO();
        Book expectedDeleteBook = bookMapper.toModel(expectedBookToDeleteDTO);

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(eq(expectedBookToDeleteDTO.getId()), any(User.class)))
                .thenReturn(Optional.of(expectedDeleteBook));

        doNothing().when(bookRepository).deleteByIdAndUser(eq(expectedBookToDeleteDTO.getId()), any(User.class));

        bookService.deleteByIdAndUser(authenticatedUser, expectedBookToDeleteDTO.getId());
        verify(bookRepository, times(1)).deleteByIdAndUser(eq(expectedBookToDeleteDTO.getId()), any(User.class));
    }

    @Test
    void whenNotExistingBookIdIsInformedThenAnExceptionShouldBeThrown() {
        BookResponseDTO expectedBookToDeleteDTO = bookResponseDTOBuilder.buildResponseBookDTO();

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(eq(expectedBookToDeleteDTO.getId()), any(User.class)))
                .thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteByIdAndUser(authenticatedUser, expectedBookToDeleteDTO.getId()));
    }

    @Test
    void whenExistingBookIsInformedThenItShowBeUpdated() {
        BookRequestDTO expectedBookToUpdateDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        BookResponseDTO expectedBookUpdateFoundDTO = bookResponseDTOBuilder.buildResponseBookDTO();
        Book expectedUpdatedBook = bookMapper.toModel(expectedBookUpdateFoundDTO);

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(eq(expectedBookToUpdateDTO.getId()), any(User.class))).thenReturn(Optional.of(expectedUpdatedBook));
        when(authorService.verifyAndGetAuthorIfExists(expectedBookToUpdateDTO.getAuthorId())).thenReturn(new Author());
        when(publisherService.verifyAndGetPublisherIfExists(expectedBookToUpdateDTO.getPublisherId())).thenReturn(new Publisher());
        when(bookRepository.save(any(Book.class))).thenReturn(expectedUpdatedBook);

        BookResponseDTO updatedBookResponseDTO = bookService.updateByIdAndUser(authenticatedUser, expectedBookToUpdateDTO.getId(), expectedBookToUpdateDTO);
        assertThat(updatedBookResponseDTO, Matchers.is(equalTo(expectedBookUpdateFoundDTO)));
    }

    @Test
    void whenNotExistingBookIsInformedThenExceptionItShowBeThrown() {
        BookRequestDTO expectedBookToUpdateDTO = bookRequestDTOBuilder.buildRequestBookDTO();

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(eq(expectedBookToUpdateDTO.getId()), any(User.class))).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.updateByIdAndUser(authenticatedUser, expectedBookToUpdateDTO.getId(), expectedBookToUpdateDTO));
    }
}
