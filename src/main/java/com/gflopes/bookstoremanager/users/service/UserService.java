package com.gflopes.bookstoremanager.users.service;

import com.gflopes.bookstoremanager.users.dto.MessageDTO;
import com.gflopes.bookstoremanager.users.dto.UserDTO;
import com.gflopes.bookstoremanager.users.entity.User;
import com.gflopes.bookstoremanager.users.exception.UserAlreadyExistsException;
import com.gflopes.bookstoremanager.users.exception.UserNotFoundException;
import com.gflopes.bookstoremanager.users.mapper.UserMapper;
import com.gflopes.bookstoremanager.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.gflopes.bookstoremanager.users.utils.MessageDTOUtils.createdMessage;
import static com.gflopes.bookstoremanager.users.utils.MessageDTOUtils.updatedMessage;

@Service
public class UserService {

    private final static UserMapper userMapper = UserMapper.INSTANCE;

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MessageDTO create(UserDTO userToCreateDTO) {
        verifyUserExists(userToCreateDTO.getEmail(), userToCreateDTO.getUsername());

        User userToCreate = userMapper.toModel(userToCreateDTO);
        userToCreate.setPassword(passwordEncoder.encode(userToCreateDTO.getPassword()));
        User createdUser = userRepository.save(userToCreate);

        return createdMessage(createdUser);
    }

    public MessageDTO update(Long id, UserDTO userToUpdateDTO) {
        User foundUser = verifyAndGetIfExists(id);

        userToUpdateDTO.setId(foundUser.getId());
        User userToUpdate = userMapper.toModel(userToUpdateDTO);
        userToUpdate.setPassword(passwordEncoder.encode(userToUpdateDTO.getPassword()));
        userToUpdate.setCreatedDate(foundUser.getCreatedDate());

        User updatedUser = userRepository.save(userToUpdate);
        return updatedMessage(updatedUser);
    }

    public void delete(Long id) {
        User user = verifyAndGetIfExists(id);
        userRepository.deleteById(user.getId());
    }

    private User verifyAndGetIfExists(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private void verifyUserExists(String email, String username) {
        userRepository.findByEmailOrUsername(email, username)
                .ifPresent(user -> { throw new UserAlreadyExistsException(user.getEmail(), user.getUsername()); });
    }
}
