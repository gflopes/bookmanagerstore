package com.gflopes.bookstoremanager.book.builder;

import com.gflopes.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.gflopes.bookstoremanager.author.dto.AuthorDTO;
import com.gflopes.bookstoremanager.book.dto.BookRequestDTO;
import com.gflopes.bookstoremanager.book.dto.BookResponseDTO;
import com.gflopes.bookstoremanager.publisher.builder.PublisherDTOBuilder;
import com.gflopes.bookstoremanager.publisher.dto.PublisherDTO;
import com.gflopes.bookstoremanager.user.builder.UserDTOBuilder;
import com.gflopes.bookstoremanager.user.dto.UserDTO;
import lombok.Builder;

@Builder
public class BookResponseDTOBuilder {


    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "O Mestre do Spring";

    @Builder.Default
    private String isbn = "978-3-16-148410-0";

    @Builder.Default
    private Integer pages = 200;

    @Builder.Default
    private Integer chapters = 10;

    @Builder.Default
    private final AuthorDTO author = AuthorDTOBuilder.builder().build().buildAuthorDTO();

    @Builder.Default
    private final PublisherDTO publisher = PublisherDTOBuilder.builder().build().buildPublisherDTO();

    private final UserDTO userDTO = UserDTOBuilder.builder().build().buildUserDTO();

    public BookResponseDTO buildResponseBookDTO() {
        return new BookResponseDTO(id,
                name,
                isbn,
                pages,
                chapters,
                author,
                publisher);
    }
}
