package com.gflopes.bookstoremanager.author.service;

import com.gflopes.bookstoremanager.author.dto.AuthorDTO;
import com.gflopes.bookstoremanager.author.entity.Author;
import com.gflopes.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.gflopes.bookstoremanager.author.exception.AuthorNotFoundException;
import com.gflopes.bookstoremanager.author.mapper.AuthorMapper;
import com.gflopes.bookstoremanager.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final static AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorDTO create(AuthorDTO authorDTO) {
        verifyAuthorExists(authorDTO.getName());

        Author authorToCreate = authorMapper.toModel(authorDTO);
        Author createdAuthor = authorRepository.save(authorToCreate);
        return authorMapper.toDTO(createdAuthor);
    }

    public AuthorDTO findById(Long id) {
        Author foundAuthor = verifyAndGetAuthorIfExists(id);
        return authorMapper.toDTO(foundAuthor);
    }

    public List<AuthorDTO> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDTO)
                .collect(Collectors.toList());

    }

    public void delete(Long id) {
        Author author = verifyAndGetAuthorIfExists(id);
        authorRepository.deleteById(id);
    }

    public Author verifyAndGetAuthorIfExists(Long id) {
        Author foundAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));

        return foundAuthor;
    }

    private void verifyAuthorExists(String authorName) {
        authorRepository.findByName(authorName)
                .ifPresent(author -> { throw new AuthorAlreadyExistsException(author.getName()); });
    }
}
