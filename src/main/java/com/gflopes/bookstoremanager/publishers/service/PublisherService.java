package com.gflopes.bookstoremanager.publishers.service;

import com.gflopes.bookstoremanager.author.mapper.AuthorMapper;
import com.gflopes.bookstoremanager.publishers.mapper.PublisherMapper;
import com.gflopes.bookstoremanager.publishers.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    private final static PublisherMapper publisherMapper = PublisherMapper.INSTANCE;

    private PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }


}
