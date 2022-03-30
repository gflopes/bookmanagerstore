package com.gflopes.bookstoremanager.author.builder;

import com.gflopes.bookstoremanager.author.dto.AuthorDTO;
import lombok.Builder;

@Builder
public class AuthorDTOBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "Gustavo Lopes";

    @Builder.Default
    private final int age = 48;

    public AuthorDTO buildAuthorDTO() {
        return new AuthorDTO(id, name, age);
    }
}
