package com.gflopes.bookstoremanager.book.mapper;

import com.gflopes.bookstoremanager.book.dto.BookRequestDTO;
import com.gflopes.bookstoremanager.book.dto.BookResponseDTO;
import com.gflopes.bookstoremanager.book.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    Book toModel(BookRequestDTO bookRequestDTO);

    Book toModel(BookResponseDTO bookResponseDTO);

    BookResponseDTO toDTO(Book book);
}
