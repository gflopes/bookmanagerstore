package com.gflopes.bookstoremanager.publishers.service;

import com.gflopes.bookstoremanager.publishers.builder.PublisherDTOBuilder;
import com.gflopes.bookstoremanager.publishers.dto.PublisherDTO;
import com.gflopes.bookstoremanager.publishers.entity.Publisher;
import com.gflopes.bookstoremanager.publishers.exception.PublisherAlreadyExistsException;
import com.gflopes.bookstoremanager.publishers.exception.PublisherNotFoundException;
import com.gflopes.bookstoremanager.publishers.mapper.PublisherMapper;
import com.gflopes.bookstoremanager.publishers.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceTest {

    private final PublisherMapper publisherMapper = PublisherMapper.INSTANCE;

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherService publisherService;

    private PublisherDTOBuilder publisherDTOBuilder;

    @BeforeEach
    void setup() {
        publisherDTOBuilder = PublisherDTOBuilder.builder().build();
    }

    @Test
    void whenNewPublisherIsInformedThenItShouldBeCreated() {
        PublisherDTO expectedPublisherToCreatedDTO = publisherDTOBuilder.buildPublisherDTO();
        Publisher expectedCreatedPublisher = publisherMapper.toModel(expectedPublisherToCreatedDTO);

        when(publisherRepository.findByNameOrCode(expectedCreatedPublisher.getName(), expectedCreatedPublisher.getCode()))
                .thenReturn(Optional.empty());
        when(publisherRepository.save(expectedCreatedPublisher)).thenReturn(expectedCreatedPublisher);

        PublisherDTO createdPublisherDTO = publisherService.create(expectedPublisherToCreatedDTO);

        assertThat(createdPublisherDTO, is(equalTo(expectedPublisherToCreatedDTO)));
    }

    @Test
    void whenExistingPublisherIsInformedThenThenExceptionShouldBeThrown() {
        PublisherDTO expectedPublisherToCreatedDTO = publisherDTOBuilder.buildPublisherDTO();
        Publisher expectedCreatedPublisher = publisherMapper.toModel(expectedPublisherToCreatedDTO);

        when(publisherRepository.findByNameOrCode(expectedCreatedPublisher.getName(), expectedCreatedPublisher.getCode()))
                .thenReturn(Optional.of(expectedCreatedPublisher));

        assertThrows(PublisherAlreadyExistsException.class, () -> publisherService.create(expectedPublisherToCreatedDTO));
    }

    @Test
    void whenValidIdIsGivenThenAndPublisherShouldBeReturned() {
        PublisherDTO expectedFounderPublisherDTO = publisherDTOBuilder.buildPublisherDTO();
        Publisher expectedFounderPublisher = publisherMapper.toModel(expectedFounderPublisherDTO);

        when(publisherRepository.findById(expectedFounderPublisherDTO.getId())).thenReturn(Optional.of(expectedFounderPublisher));

        PublisherDTO founderPublisherDTO = publisherService.findById(expectedFounderPublisherDTO.getId());

        assertThat(founderPublisherDTO, is(equalTo(expectedFounderPublisherDTO)));
    }

    @Test
    void whenInvalidIdIsGivenThenAndPublisherShouldBeThrow() {
        PublisherDTO expectedFounderPublisherDTO = publisherDTOBuilder.buildPublisherDTO();

        when(publisherRepository.findById(expectedFounderPublisherDTO.getId())).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> publisherService.findById(expectedFounderPublisherDTO.getId()));
    }

    @Test
    void whenListPublishersIsCalledThenItShouldBeReturned() {
        PublisherDTO expectedFounderPublisherDTO = publisherDTOBuilder.buildPublisherDTO();
        Publisher expectedFounderPublisher = publisherMapper.toModel(expectedFounderPublisherDTO);

        when(publisherRepository.findAll()).thenReturn(Collections.singletonList(expectedFounderPublisher));

        List<PublisherDTO> foundersPublishersDTO = publisherService.findAll();
        assertThat(foundersPublishersDTO.size(), is(1));
        assertThat(foundersPublishersDTO.get(0), equalTo(expectedFounderPublisherDTO));
    }

    @Test
    void whenListPublishersIsCalledThenAnEmptyListShouldBeReturned() {
        when(publisherRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        List<PublisherDTO> foundersPublishersDTO = publisherService.findAll();

        assertThat(foundersPublishersDTO.size(), is(0));
    }

    @Test
    void whenValidPublisherIdIsGivenThenItShouldBeDeleted() {
        PublisherDTO expectedDeletedPublisherDTO = publisherDTOBuilder.buildPublisherDTO();
        Publisher expectedDeletedPublisher = publisherMapper.toModel(expectedDeletedPublisherDTO);

        Long expectedDeletedPublisherId = expectedDeletedPublisherDTO.getId();

        doNothing().when(publisherRepository).deleteById(expectedDeletedPublisherId);
        when(publisherRepository.findById(expectedDeletedPublisherId)).thenReturn(Optional.of(expectedDeletedPublisher));

        publisherService.delete(expectedDeletedPublisherId);

        verify(publisherRepository, times(1)).deleteById(expectedDeletedPublisherId);
    }

    @Test
    void whenInvalidPublisherIdIsGivenThenItShouldBeThrown() {
        var expectedInvalidPublisherId = 2L;

        when(publisherRepository.findById(expectedInvalidPublisherId)).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> publisherService.delete(expectedInvalidPublisherId));
    }
}
