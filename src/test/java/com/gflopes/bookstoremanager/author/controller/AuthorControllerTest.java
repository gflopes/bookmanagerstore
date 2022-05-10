package com.gflopes.bookstoremanager.author.controller;

import com.gflopes.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.gflopes.bookstoremanager.author.dto.AuthorDTO;
import com.gflopes.bookstoremanager.author.service.AuthorService;
import com.gflopes.bookstoremanager.util.JsonConversionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static org.hamcrest.core.Is.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthorControllerTest {

    private static final String AUTHORS_API_URL_PATH = "/api/v1/authors";

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private MockMvc mockMvc;

    private AuthorDTOBuilder authorDTOBuilder;

    @BeforeEach
    void setUp() {
        authorDTOBuilder = AuthorDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(authorController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenStatusCreatedShouldByReturned() throws Exception {
        AuthorDTO expectedCreatedAuthorDTO = authorDTOBuilder.buildAuthorDTO();

        Mockito.when(authorService.create(expectedCreatedAuthorDTO))
                .thenReturn(expectedCreatedAuthorDTO);

        mockMvc.perform(post(AUTHORS_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(expectedCreatedAuthorDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(expectedCreatedAuthorDTO.getId().intValue())))
                    .andExpect(jsonPath("$.name", is(expectedCreatedAuthorDTO.getName())))
                    .andExpect(jsonPath("$.age", is(expectedCreatedAuthorDTO.getAge())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenBadRequestStatusShouldBeInformed() throws Exception {
        AuthorDTO expectedCreatedAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        expectedCreatedAuthorDTO.setName(null);

        mockMvc.perform(post(AUTHORS_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(expectedCreatedAuthorDTO)))
                    .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETWithValidIdIsCalledThenStatusOkShouldBeReturned() throws Exception {
        AuthorDTO expectedFoundAuthorDTO = authorDTOBuilder.buildAuthorDTO();

        Mockito.when(authorService.findById(expectedFoundAuthorDTO.getId()))
                .thenReturn(expectedFoundAuthorDTO);

        mockMvc.perform(get(AUTHORS_API_URL_PATH + "/" + expectedFoundAuthorDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedFoundAuthorDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedFoundAuthorDTO.getName())))
                .andExpect(jsonPath("$.age", is(expectedFoundAuthorDTO.getAge())));
    }

    @Test
    void whenGETListIsCalledThenStatusOkShouldBeReturned() throws Exception {
        AuthorDTO expectedFoundAuthorDTO = authorDTOBuilder.buildAuthorDTO();

        Mockito.when(authorService.findAll())
                .thenReturn(Collections.singletonList(expectedFoundAuthorDTO));

        mockMvc.perform(get(AUTHORS_API_URL_PATH)
                 .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(expectedFoundAuthorDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedFoundAuthorDTO.getName())))
                .andExpect(jsonPath("$[0].age", is(expectedFoundAuthorDTO.getAge())));
    }

    @Test
    void whenDELETEWithValidIdIsCalledThenStatusOkShouldBeReturned() throws Exception {
        AuthorDTO expectedDeleteAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        var expectedDeleteAuthorId = expectedDeleteAuthorDTO.getId();

        doNothing().when(authorService).delete(expectedDeleteAuthorId);

        mockMvc.perform(delete(AUTHORS_API_URL_PATH + "/" + expectedDeleteAuthorId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}