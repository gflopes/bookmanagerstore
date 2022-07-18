package com.gflopes.bookstoremanager.users.service;

import com.gflopes.bookstoremanager.users.dto.MessageDTO;
import com.gflopes.bookstoremanager.users.dto.UserDTO;
import com.gflopes.bookstoremanager.users.entity.User;
import com.gflopes.bookstoremanager.users.exception.UserAlreadyExistsException;
import com.gflopes.bookstoremanager.users.exception.UserNotFoundException;
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

    public MessageDTO create(UserDTO userToCreateDTO) {
        verifyUserExists(userToCreateDTO.getEmail(), userToCreateDTO.getUsername());

        User userToCreate = userMapper.toModel(userToCreateDTO);
        User createdUser = userRepository.save(userToCreate);

        return returnMessage(createdUser, "created");
    }

    public MessageDTO update(Long id, UserDTO userToUpdateDTO) {
        User foundUser = verifyAndGetIfExists(id);

        userToUpdateDTO.setId(foundUser.getId());
        User userToUpdate = userMapper.toModel(userToUpdateDTO);
        userToUpdate.setCreatedDate(foundUser.getCreatedDate());
        User updatedUser = userRepository.save(userToUpdate);

        return returnMessage(updatedUser, "updated");
    }

    private MessageDTO returnMessage(User updateUser, String action) {
        String updatedUsername = updateUser.getUsername();
        Long updateId = updateUser.getId();
        String updateStringUserMessage = String.format("User %s with ID %s successfully %s", updatedUsername, updateId, action  );
        return MessageDTO.builder()
                .message(updateStringUserMessage)
                .build();
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
