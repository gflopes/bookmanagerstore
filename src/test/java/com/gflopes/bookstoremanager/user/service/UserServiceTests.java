package com.gflopes.bookstoremanager.user.service;

import com.gflopes.bookstoremanager.user.builder.UserDTOBuilder;
import com.gflopes.bookstoremanager.user.dto.MessageDTO;
import com.gflopes.bookstoremanager.user.dto.UserDTO;
import com.gflopes.bookstoremanager.user.entity.User;
import com.gflopes.bookstoremanager.user.exception.UserAlreadyExistsException;
import com.gflopes.bookstoremanager.user.exception.UserNotFoundException;
import com.gflopes.bookstoremanager.user.mapper.UserMapper;
import com.gflopes.bookstoremanager.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private PasswordEncoder passwordEncoder;

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
        when(passwordEncoder.encode(expectedCreatedUser.getPassword())).thenReturn(expectedCreatedUser.getPassword());
        when(userRepository.save(expectedCreatedUser)).thenReturn(expectedCreatedUser);

        MessageDTO creationMessage = userService.create(expectedUserToCreatedDTO);

        assertThat(expectedCreationMessage, is(equalTo(creationMessage.getMessage())));
    }

    @Test
    void whenExistingUserIsInformedThenAUserExceptionShouldBeThrown() {
        UserDTO expectedUserToCreatedDTO = userDTOBuilder.buildUserDTO();
        User expectedCreatedUser = userMapper.toModel(expectedUserToCreatedDTO);

        when(userRepository.findByEmailOrUsername(expectedCreatedUser.getEmail(), expectedCreatedUser.getUsername()))
                .thenReturn(Optional.of(expectedCreatedUser));

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(expectedUserToCreatedDTO));
    }

    @Test
    void whenInvalidUserIdIsGivenThenItShouldBeThrown() {
        UserDTO expectedUserToFindDTO = userDTOBuilder.buildUserDTO();
        Long expectedFoundUserId = expectedUserToFindDTO.getId();

        when(userRepository.findById(expectedFoundUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(expectedFoundUserId));
    }

    @Test
    void whenExistingUserIsInformedThenItShowBeUpdated() {
        UserDTO expectedUserToUpdatedDTO = userDTOBuilder.buildUserDTO();
        expectedUserToUpdatedDTO.setUsername("gflopes22update");
        User expectedUpdatedUser = userMapper.toModel(expectedUserToUpdatedDTO);
        String expectedUpdateMessage = "User gflopes22update with ID 1 successfully updated";

        when(userRepository.findById(expectedUserToUpdatedDTO.getId())).thenReturn(Optional.of(expectedUpdatedUser));
        when(passwordEncoder.encode(expectedUpdatedUser.getPassword())).thenReturn(expectedUpdatedUser.getPassword());
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
