package com.gflopes.bookstoremanager.user.service;

import com.gflopes.bookstoremanager.jwt.dto.JwtRequest;
import com.gflopes.bookstoremanager.jwt.dto.JwtResponse;
import com.gflopes.bookstoremanager.jwt.service.JwtTokenManager;
import com.gflopes.bookstoremanager.user.builder.JwtRequestBuilder;
import com.gflopes.bookstoremanager.user.builder.UserDTOBuilder;
import com.gflopes.bookstoremanager.user.dto.UserDTO;
import com.gflopes.bookstoremanager.user.entity.User;
import com.gflopes.bookstoremanager.user.mapper.UserMapper;
import com.gflopes.bookstoremanager.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenManager jwtTokenManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private JwtRequestBuilder jwtRequestBuilder;

    private UserDTOBuilder userDTOBuilder;

    @BeforeEach
    void setup() {
        jwtRequestBuilder = JwtRequestBuilder.builder().build();
        userDTOBuilder = UserDTOBuilder.builder().build();
    }

    @Test
    void whenUsernameAndPasswordIsInformedThenTokenShouldBeGenerated() {
        JwtRequest jwtRequest = jwtRequestBuilder.buildJwtRequest();
        UserDTO expectedFoundUserDTO = userDTOBuilder.buildUserDTO();
        User expectedFoundUser = userMapper.toModel(expectedFoundUserDTO);
        String expectedGeneratedToken = "fakeToken";

        when(userRepository.findByUsername(jwtRequest.getUsername())).thenReturn(Optional.of(expectedFoundUser));
        when(jwtTokenManager.generateToken(any(UserDetails.class))).thenReturn(expectedGeneratedToken);

        JwtResponse generatedTokenResponse = authenticationService.createAuthenticationToken(jwtRequest);

        assertThat(generatedTokenResponse.getJwtToken(), is(equalTo(expectedGeneratedToken)));
    }

    @Test
    void whenUsernameIsInformedThenUserShowBeReturned() {
        UserDTO expectedFoundUserDTO = userDTOBuilder.buildUserDTO();
        User expectedFoundUser = userMapper.toModel(expectedFoundUserDTO);
        SimpleGrantedAuthority expectedUserRole = new SimpleGrantedAuthority("ROLE_" + expectedFoundUserDTO.getRole().getDescription());
        String expectedUsername = expectedFoundUserDTO.getUsername();

        when(userRepository.findByUsername(expectedUsername)).thenReturn(Optional.of(expectedFoundUser));

        UserDetails userDetails = authenticationService.loadUserByUsername(expectedUsername);

        assertThat(userDetails.getUsername(), is(equalTo(expectedFoundUser.getUsername())));
        assertThat(userDetails.getPassword(), is(equalTo(expectedFoundUser.getPassword())));
        assertTrue(userDetails.getAuthorities().contains(expectedUserRole));
    }

    @Test
    void whenInvalidUsernameIsInformedThenAnExceptionUserShowBeThrows() {
        UserDTO expectedFoundUserDTO = userDTOBuilder.buildUserDTO();
        String expectedUsername = expectedFoundUserDTO.getUsername();

        when(userRepository.findByUsername(expectedUsername)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.loadUserByUsername(expectedUsername));
    }
}
