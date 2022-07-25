package com.gflopes.bookstoremanager.user.controller;

import com.gflopes.bookstoremanager.jwt.dto.JwtRequest;
import com.gflopes.bookstoremanager.jwt.dto.JwtResponse;
import com.gflopes.bookstoremanager.user.builder.JwtRequestBuilder;
import com.gflopes.bookstoremanager.user.builder.UserDTOBuilder;
import com.gflopes.bookstoremanager.user.dto.MessageDTO;
import com.gflopes.bookstoremanager.user.dto.UserDTO;
import com.gflopes.bookstoremanager.user.service.AuthenticationService;
import com.gflopes.bookstoremanager.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static com.gflopes.bookstoremanager.util.JsonConversionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    private static final String USERS_API_URL_PATH = "/api/v1/users";

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private UserController userController;

    private UserDTOBuilder userDTOBuilder;

    private JwtRequestBuilder jwtRequestBuilder;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        jwtRequestBuilder = JwtRequestBuilder.builder().build();
        userDTOBuilder = UserDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenStatusCreatedShouldByReturned() throws Exception {
        UserDTO expectedCreatedUserDTO = userDTOBuilder.buildUserDTO();
        String expectedCreationMessage = "User gflopes22 with ID 1 successfully created";
        MessageDTO expectedCreationMessageDTO = MessageDTO.builder().message(expectedCreationMessage).build();

        when(userService.create(expectedCreatedUserDTO))
                .thenReturn(expectedCreationMessageDTO);

        mockMvc.perform(post(USERS_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedCreatedUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(expectedCreationMessage)));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenBadRequestStatusShouldBeInformed() throws Exception {
        UserDTO expectedCreatedUserDTO = userDTOBuilder.buildUserDTO();
        expectedCreatedUserDTO.setUsername(null);

        mockMvc.perform(post(USERS_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedCreatedUserDTO)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    void whenDELETEWithValidIdIsCalledThenStatusOkShouldBeReturned() throws Exception {
        UserDTO expectedDeleteUserDTO = userDTOBuilder.buildUserDTO();
        var expectedDeleteUserId = expectedDeleteUserDTO.getId();

        doNothing().when(userService).delete(expectedDeleteUserId);

        mockMvc.perform(delete(USERS_API_URL_PATH + "/" + expectedDeleteUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenPUTIsCalledThenStatusCreatedShouldByReturned() throws Exception {
        UserDTO expectedUpdatedUserDTO = userDTOBuilder.buildUserDTO();
        expectedUpdatedUserDTO.setUsername("gflopes22update");
        String expectedUpdatedMessage = "User gflopes22update with ID 1 successfully updated";
        MessageDTO expectedUpdatedMessageDTO = MessageDTO.builder().message(expectedUpdatedMessage).build();
        var expectedUserIdUpdated = expectedUpdatedUserDTO.getId();

        when(userService.update(expectedUserIdUpdated, expectedUpdatedUserDTO))
                .thenReturn(expectedUpdatedMessageDTO);

        mockMvc.perform(put(USERS_API_URL_PATH + "/" + expectedUserIdUpdated)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedUpdatedUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(expectedUpdatedMessage)));
    }

    @Test
    void whenPOSTIsCalledToAuthenticateUserThenOkShouldBeInformed() throws Exception {
        JwtRequest jwtRequest = jwtRequestBuilder.buildJwtRequest();
        JwtResponse expectedJwtToken = JwtResponse.builder().jwtToken("testToken").build();

        when(authenticationService.createAuthenticationToken(jwtRequest)).thenReturn(expectedJwtToken);

        mockMvc.perform(post(USERS_API_URL_PATH + "/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(jwtRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken", is(expectedJwtToken.getJwtToken())));
    }

    @Test
    void whenPOSTIsCalledToAuthenticateUserWithoutPasswordThenBadRequestShouldBeInformed() throws Exception {
        JwtRequest jwtRequest = jwtRequestBuilder.buildJwtRequest();
        jwtRequest.setPassword(null);

        mockMvc.perform(post(USERS_API_URL_PATH + "/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(jwtRequest)))
                .andExpect(status().isBadRequest());
    }
}
