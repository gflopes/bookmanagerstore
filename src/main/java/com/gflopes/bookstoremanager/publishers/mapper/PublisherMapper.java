package com.gflopes.bookstoremanager.publishers;

import com.gflopes.bookstoremanager.author.dto.AuthorDTO;
import com.gflopes.bookstoremanager.author.entity.Author;
import com.gflopes.bookstoremanager.publishers.entity.Publisher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PublisherMapper {

    PublisherMapper INSTANCE = Mappers.getMapper(PublisherMapper.class);

    Publisher toModel(PublisherDTO publisherDTO);

    PublisherDTO toDTO(Publisher publisher);
}
