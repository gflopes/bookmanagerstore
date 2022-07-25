package com.gflopes.bookstoremanager.book.controller;

import com.gflopes.bookstoremanager.book.builder.BookRequestDTOBuilder;
import com.gflopes.bookstoremanager.book.builder.BookResponseDTOBuilder;
import com.gflopes.bookstoremanager.book.dto.BookRequestDTO;
import com.gflopes.bookstoremanager.book.dto.BookResponseDTO;
import com.gflopes.bookstoremanager.book.service.BookService;
import com.gflopes.bookstoremanager.user.dto.AuthenticatedUser;
import org.hibernate.annotations.CollectionId;
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

import java.util.Collections;

import static com.gflopes.bookstoremanager.util.JsonConversionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookControllerTests {

    private static final String BOOKS_API_URL_PATH = "/api/v1/books";

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    private BookRequestDTOBuilder bookRequestDTOBuilder;

    private BookResponseDTOBuilder bookResponseDTOBuilder;

    @BeforeEach
    void setUp() {
        bookRequestDTOBuilder = BookRequestDTOBuilder.builder().build();
        bookResponseDTOBuilder = BookResponseDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenStatusCreatedShouldByReturned() throws Exception {
        BookRequestDTO expectedBookToCreateDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        BookResponseDTO expectedCreatedBookDTO = bookResponseDTOBuilder.buildResponseBookDTO();

        when(bookService.create(any(AuthenticatedUser.class), eq(expectedBookToCreateDTO))).thenReturn(expectedCreatedBookDTO);

        mockMvc.perform(post(BOOKS_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedBookToCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedCreatedBookDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedCreatedBookDTO.getName())))
                .andExpect(jsonPath("$.isbn", is(expectedCreatedBookDTO.getIsbn())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenBadRequestStatusShouldBeInformed() throws Exception {
        BookRequestDTO expectedBookToCreateDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        expectedBookToCreateDTO.setIsbn(null);

        mockMvc.perform(post(BOOKS_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedBookToCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETIsCalledThenStatusOkShouldBeReturned() throws Exception {
        BookRequestDTO expectedBookFindDTO = bookRequestDTOBuilder.buildRequestBookDTO();
        BookResponseDTO expectedBookFoundDTO = bookResponseDTOBuilder.buildResponseBookDTO();

        when(bookService.findByIdAndUser(any(AuthenticatedUser.class), eq(expectedBookFindDTO.getId()))).thenReturn(expectedBookFoundDTO);

        mockMvc.perform(get(BOOKS_API_URL_PATH + "/" + expectedBookFindDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBookFoundDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedBookFoundDTO.getName())))
                .andExpect(jsonPath("$.isbn", is(expectedBookFoundDTO.getIsbn())));
    }

    @Test
    void whenGETLisIsCalledThenStatusOkShouldBeReturned() throws Exception {
        BookResponseDTO expectedBookFoundDTO = bookResponseDTOBuilder.buildResponseBookDTO();

        when(bookService.findAllByUser(any(AuthenticatedUser.class))).thenReturn(Collections.singletonList(expectedBookFoundDTO));

        mockMvc.perform(get(BOOKS_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(expectedBookFoundDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedBookFoundDTO.getName())))
                .andExpect(jsonPath("$[0].isbn", is(expectedBookFoundDTO.getIsbn())));
    }

}
