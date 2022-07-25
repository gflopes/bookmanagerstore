package com.gflopes.bookstoremanager.book.builder;

import com.gflopes.bookstoremanager.book.dto.BookRequestDTO;
import com.gflopes.bookstoremanager.user.builder.UserDTOBuilder;
import com.gflopes.bookstoremanager.user.dto.UserDTO;
import lombok.Builder;

@Builder
public class BookRequestDTOBuilder {

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
    private Long authorId = 1L;

    @Builder.Default
    private Long publisherId = 1L;

    private final UserDTO userDTO = UserDTOBuilder.builder().build().buildUserDTO();

    public BookRequestDTO buildRequestBookDTO() {
        return new BookRequestDTO(id,
                name,
                isbn,
                pages,
                chapters,
                authorId,
                publisherId);
    }
}
