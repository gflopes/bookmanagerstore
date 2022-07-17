package com.gflopes.bookstoremanager.publishers.builder;

import com.gflopes.bookstoremanager.publishers.dto.PublisherDTO;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public class PublisherDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Gustavo Lopes Editora Ltda";

    @Builder.Default
    private String code = "GUZAO1234";

    @Builder.Default
    private LocalDate foundationDate = LocalDate.of(2022, 05, 10);

    public PublisherDTO buildPublisherDTO() {
        return new PublisherDTO(id, name, code, foundationDate);
    }
}
