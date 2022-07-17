package com.gflopes.bookstoremanager.users.service;

import com.gflopes.bookstoremanager.publishers.mapper.PublisherMapper;
import com.gflopes.bookstoremanager.publishers.repository.PublisherRepository;
import com.gflopes.bookstoremanager.users.mapper.UserMapper;
import com.gflopes.bookstoremanager.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final static UserMapper userMapper = UserMapper.INSTANCE;

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
