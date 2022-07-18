package com.gflopes.bookstoremanager.users.controller;

import com.gflopes.bookstoremanager.publishers.dto.PublisherDTO;
import com.gflopes.bookstoremanager.users.builder.UserDTOBuilder;
import com.gflopes.bookstoremanager.users.dto.MessageDTO;
import com.gflopes.bookstoremanager.users.dto.UserDTO;
import com.gflopes.bookstoremanager.users.service.UserService;
import com.gflopes.bookstoremanager.util.JsonConversionUtils;
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

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    private static final String USERS_API_URL_PATH = "/api/v1/users";

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserDTOBuilder userDTOBuilder;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
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
                        .content(JsonConversionUtils.asJsonString(expectedCreatedUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(expectedCreationMessage)));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenBadRequestStatusShouldBeInformed() throws Exception {
        UserDTO expectedCreatedUserDTO = userDTOBuilder.buildUserDTO();
        expectedCreatedUserDTO.setUsername(null);

        mockMvc.perform(post(USERS_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConversionUtils.asJsonString(expectedCreatedUserDTO)))
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
}
