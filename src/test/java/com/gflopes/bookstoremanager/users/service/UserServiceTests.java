package com.gflopes.bookstoremanager.users.service;

import com.gflopes.bookstoremanager.users.builder.UserDTOBuilder;
import com.gflopes.bookstoremanager.users.dto.MessageDTO;
import com.gflopes.bookstoremanager.users.dto.UserDTO;
import com.gflopes.bookstoremanager.users.entity.User;
import com.gflopes.bookstoremanager.users.exception.UserAlreadyExistsException;
import com.gflopes.bookstoremanager.users.exception.UserNotFoundException;
import com.gflopes.bookstoremanager.users.mapper.UserMapper;
import com.gflopes.bookstoremanager.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserDTOBuilder userDTOBuilder;

    @BeforeEach
    void setUp() {
        userDTOBuilder = UserDTOBuilder.builder().build();
    }

    @Test
    void whenNewUserIsInformedThenItShouldBeCreated() {
        UserDTO expectedUserToCreatedDTO = userDTOBuilder.buildUserDTO();
        User expectedCreatedUser = userMapper.toModel(expectedUserToCreatedDTO);
        String expectedCreationMessage = "User gflopes22 with ID 1 successfully created";

        when(userRepository.findByEmailOrUsername(expectedCreatedUser.getEmail(), expectedCreatedUser.getUsername()))
                .thenReturn(Optional.empty());
        when(userRepository.save(expectedCreatedUser)).thenReturn(expectedCreatedUser);

        MessageDTO creationMessage = userService.create(expectedUserToCreatedDTO);

        assertThat(expectedCreationMessage, is(equalTo(creationMessage.getMessage())));
    }

    @Test
    void whenExistingUserIsInformedThenThenExceptionShouldBeThrown() {
        UserDTO expectedUserToCreatedDTO = userDTOBuilder.buildUserDTO();
        User expectedCreatedUser = userMapper.toModel(expectedUserToCreatedDTO);

        when(userRepository.findByEmailOrUsername(expectedCreatedUser.getEmail(), expectedCreatedUser.getUsername()))
                .thenReturn(Optional.of(expectedCreatedUser));

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(expectedUserToCreatedDTO));
    }

    @Test
    void whenInvalidUserIdIsGivenThenItShouldBeThrown() {
        UserDTO expectedUserToDeletedDTO = userDTOBuilder.buildUserDTO();
        Long expectedDeletedUserId = expectedUserToDeletedDTO.getId();

        when(userRepository.findById(expectedDeletedUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(expectedDeletedUserId));
    }

    @Test
    void whenExistingUserIsInformedThenItShowBeUpdated() {
        UserDTO expectedUserToUpdatedDTO = userDTOBuilder.buildUserDTO();
        expectedUserToUpdatedDTO.setUsername("gflopes22update");
        User expectedUpdatedUser = userMapper.toModel(expectedUserToUpdatedDTO);
        String expectedUpdateMessage = "User gflopes22update with ID 1 successfully updated";

        when(userRepository.findById(expectedUserToUpdatedDTO.getId())).thenReturn(Optional.of(expectedUpdatedUser));
        when(userRepository.save(expectedUpdatedUser)).thenReturn(expectedUpdatedUser);

        MessageDTO successUpdateMessage = userService.update(expectedUserToUpdatedDTO.getId(), expectedUserToUpdatedDTO);

        assertThat(successUpdateMessage.getMessage(), is(equalTo(expectedUpdateMessage)));
    }

    @Test
    void whenNotExistingUserIsInformedThenAndExceptionItShowBeThrow() {
        UserDTO expectedUserToUpdatedDTO = userDTOBuilder.buildUserDTO();
        expectedUserToUpdatedDTO.setUsername("gflopes22update");
        User expectedUpdatedUser = userMapper.toModel(expectedUserToUpdatedDTO);
        String expectedUpdateMessage = "User gflopes22update with ID 1 successfully updated";

        when(userRepository.findById(expectedUserToUpdatedDTO.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(expectedUserToUpdatedDTO.getId(), expectedUserToUpdatedDTO));
    }

}
