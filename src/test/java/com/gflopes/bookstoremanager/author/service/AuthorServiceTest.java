package com.gflopes.bookstoremanager.author.service;

import com.gflopes.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.gflopes.bookstoremanager.author.dto.AuthorDTO;
import com.gflopes.bookstoremanager.author.entity.Author;
import com.gflopes.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.gflopes.bookstoremanager.author.exception.AuthorNotFoundException;
import com.gflopes.bookstoremanager.author.mapper.AuthorMapper;
import com.gflopes.bookstoremanager.author.repository.AuthorRepository;

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
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    private final AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private AuthorDTOBuilder authorDTOBuilder;

    @BeforeEach
    void setUp() {
        authorDTOBuilder = AuthorDTOBuilder.builder().build();
    }

    @Test
    void whenNewAuthorIsInformedThenItShouldBeCreated() {
        AuthorDTO expectedAuthorToCreatedDTO = authorDTOBuilder.buildAuthorDTO();
        Author expectedCreatedAuthor = authorMapper.toModel(expectedAuthorToCreatedDTO);

        when(authorRepository.save(expectedCreatedAuthor)).thenReturn(expectedCreatedAuthor);
        when(authorRepository.findByName(expectedCreatedAuthor.getName())).thenReturn(Optional.empty());

        AuthorDTO createdAuthorDTO = authorService.create(expectedAuthorToCreatedDTO);

        assertThat(createdAuthorDTO, is(equalTo(expectedAuthorToCreatedDTO)));
    }

    @Test
    void whenExistingAuthorIsInformedThenThenExceptionShouldBeThrown() {
        AuthorDTO expectedAuthorToCreatedDTO = authorDTOBuilder.buildAuthorDTO();
        Author expectedCreatedAuthor = authorMapper.toModel(expectedAuthorToCreatedDTO);

        when(authorRepository.findByName(expectedCreatedAuthor.getName())).thenReturn(Optional.of(expectedCreatedAuthor));

        assertThrows(AuthorAlreadyExistsException.class, () -> authorService.create(expectedAuthorToCreatedDTO));
    }

    @Test
    void whenValidIdIsGivenThenAndAuthorShouldBeReturned() {
        AuthorDTO expectedFounderAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        Author expectedFounderAuthor = authorMapper.toModel(expectedFounderAuthorDTO);

        when(authorRepository.findById(expectedFounderAuthorDTO.getId())).thenReturn(Optional.of(expectedFounderAuthor));

        AuthorDTO founderAuthorDTO = authorService.findById(expectedFounderAuthorDTO.getId());

        assertThat(founderAuthorDTO, is(equalTo(expectedFounderAuthorDTO)));
    }

    @Test
    void whenInvalidIdIsGivenThenAndAuthorShouldBeThrow() {
        AuthorDTO expectedFounderAuthorDTO = authorDTOBuilder.buildAuthorDTO();

        when(authorRepository.findById(expectedFounderAuthorDTO.getId())).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorService.findById(expectedFounderAuthorDTO.getId()));
    }

    @Test
    void whenListAuthorsIsCalledThenItShouldBeReturned() {
        AuthorDTO expectedFounderAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        Author expectedFounderAuthor = authorMapper.toModel(expectedFounderAuthorDTO);

        when(authorRepository.findAll()).thenReturn(Collections.singletonList(expectedFounderAuthor));

        List<AuthorDTO> foundersAuthorsDTO = authorService.findAll();

        assertThat(foundersAuthorsDTO.size(), is(1));
        assertThat(foundersAuthorsDTO.get(0), equalTo(expectedFounderAuthorDTO));
    }

    @Test
    void whenListAuthorsIsCalledThenAnEmptyListShouldBeReturned() {
        when(authorRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        List<AuthorDTO> foundersAuthorsDTO = authorService.findAll();

        assertThat(foundersAuthorsDTO.size(), is(0));
    }

    @Test
    void whenValidAuthorIdIsGivenThenItShouldBeDeleted() {
        AuthorDTO expectedDeletedAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        Author expectedDeletedAuthor = authorMapper.toModel(expectedDeletedAuthorDTO);

        Long expectedDeletedAuthorId = expectedDeletedAuthorDTO.getId();

        doNothing().when(authorRepository).deleteById(expectedDeletedAuthorId);
        when(authorRepository.findById(expectedDeletedAuthorId)).thenReturn(Optional.of(expectedDeletedAuthor));

        authorService.delete(expectedDeletedAuthorId);

        verify(authorRepository, times(1)).deleteById(expectedDeletedAuthorId);
    }

    @Test
    void whenInvalidAuthorIdIsGivenThenItShouldBeThrown() {
        var expectedInvalidAuthorId = 2L;

        when(authorRepository.findById(expectedInvalidAuthorId)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorService.delete(expectedInvalidAuthorId));
    }
}