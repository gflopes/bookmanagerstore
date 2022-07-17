package com.gflopes.bookstoremanager.publishers.controller;

import com.gflopes.bookstoremanager.author.dto.AuthorDTO;
import com.gflopes.bookstoremanager.publishers.builder.PublisherDTOBuilder;
import com.gflopes.bookstoremanager.publishers.dto.PublisherDTO;
import com.gflopes.bookstoremanager.publishers.service.PublisherService;
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

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PublisherControllerTest {

    private static final String PUBLISHERS_API_URL_PATH = "/api/v1/publishers";

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private PublisherController publisherController;

    private PublisherDTOBuilder publisherDTOBuilder;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        publisherDTOBuilder = PublisherDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(publisherController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenStatusCreatedShouldByReturned() throws Exception {
        PublisherDTO expectedCreatedPublisherDTO = publisherDTOBuilder.buildPublisherDTO();

        when(publisherService.create(expectedCreatedPublisherDTO))
                .thenReturn(expectedCreatedPublisherDTO);

        mockMvc.perform(post(PUBLISHERS_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConversionUtils.asJsonString(expectedCreatedPublisherDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedCreatedPublisherDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedCreatedPublisherDTO.getName())))
                .andExpect(jsonPath("$.code", is(expectedCreatedPublisherDTO.getCode())));
    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldThenBadRequestStatusShouldBeInformed() throws Exception {
        PublisherDTO expectedCreatedPublisherDTO = publisherDTOBuilder.buildPublisherDTO();
        expectedCreatedPublisherDTO.setName(null);

        mockMvc.perform(post(PUBLISHERS_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonConversionUtils.asJsonString(expectedCreatedPublisherDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGETWithValidIdIsCalledThenStatusOkShouldBeReturned() throws Exception {
        PublisherDTO expectedFoundPublisherDTO = publisherDTOBuilder.buildPublisherDTO();
        Long expectedFoundPublisherDTOId = expectedFoundPublisherDTO.getId();

        when(publisherService.findById(expectedFoundPublisherDTOId)).thenReturn(expectedFoundPublisherDTO);

        mockMvc.perform(get(PUBLISHERS_API_URL_PATH + "/" + expectedFoundPublisherDTOId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedFoundPublisherDTOId.intValue())))
                .andExpect(jsonPath("$.name", is(expectedFoundPublisherDTO.getName())))
                .andExpect(jsonPath("$.code", is(expectedFoundPublisherDTO.getCode())));
    }

    @Test
    void whenGETListIsCalledThenStatusOkShouldBeReturned() throws Exception {
        PublisherDTO expectedFoundPublishersDTO = publisherDTOBuilder.buildPublisherDTO();

        when(publisherService.findAll())
                .thenReturn(Collections.singletonList(expectedFoundPublishersDTO));

        mockMvc.perform(get(PUBLISHERS_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(expectedFoundPublishersDTO.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedFoundPublishersDTO.getName())))
                .andExpect(jsonPath("$[0].code", is(expectedFoundPublishersDTO.getCode())));
    }

    @Test
    void whenDELETEWithValidIdIsCalledThenStatusOkShouldBeReturned() throws Exception {
        PublisherDTO expectedDeletePublisherDTO = publisherDTOBuilder.buildPublisherDTO();
        var expectedDeletePublisherId = expectedDeletePublisherDTO.getId();

        doNothing().when(publisherService).delete(expectedDeletePublisherId);

        mockMvc.perform(delete(PUBLISHERS_API_URL_PATH + "/" + expectedDeletePublisherId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
