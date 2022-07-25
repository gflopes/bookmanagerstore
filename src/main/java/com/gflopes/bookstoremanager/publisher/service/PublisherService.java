package com.gflopes.bookstoremanager.publisher.service;

import com.gflopes.bookstoremanager.publisher.dto.PublisherDTO;
import com.gflopes.bookstoremanager.publisher.entity.Publisher;
import com.gflopes.bookstoremanager.publisher.exception.PublisherAlreadyExistsException;
import com.gflopes.bookstoremanager.publisher.exception.PublisherNotFoundException;
import com.gflopes.bookstoremanager.publisher.mapper.PublisherMapper;
import com.gflopes.bookstoremanager.publisher.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private final static PublisherMapper publisherMapper = PublisherMapper.INSTANCE;

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public PublisherDTO create(PublisherDTO publisherDTO) {
        verifyPublisherExists(publisherDTO.getName(), publisherDTO.getCode());

        Publisher publisher = publisherMapper.toModel(publisherDTO);
        Publisher createdPublisher = publisherRepository.save(publisher);
        return publisherMapper.toDTO(createdPublisher);
    }

    public PublisherDTO findById(Long id) {
        return publisherRepository.findById(id)
                .map(publisherMapper::toDTO)
                .orElseThrow(() -> new PublisherNotFoundException(id));
    }

    public List<PublisherDTO> findAll() {
        return publisherRepository.findAll()
                .stream()
                .map(publisherMapper::toDTO)
                .collect(Collectors.toList());

    }

    public void delete(Long id) {
        Publisher publisher = verifyAndGetPublisherIfExists(id);
        publisherRepository.deleteById(publisher.getId());
    }

    public Publisher verifyAndGetPublisherIfExists(Long id) {
        Publisher foundPublisher = publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException(id));

        return foundPublisher;
    }
    private void verifyPublisherExists(String name, String code) {
        publisherRepository.findByNameOrCode(name, code)
                .ifPresent(publisher -> { throw new PublisherAlreadyExistsException(publisher.getName(), publisher.getCode()); });
    }

}
