package com.gflopes.bookstoremanager.publisher.controller.docs;

import com.gflopes.bookstoremanager.publisher.dto.PublisherDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "publishers", description = "Publishers Management")
public interface PublisherControllerDocs {

    @Operation(summary = "Publishers create operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success created publisher"),
            @ApiResponse(responseCode = "400", description = "Missing required field(s), wrong field(s) range value or publisher already registered on system")
    })
    PublisherDTO create(PublisherDTO publisherDTO);

    @Operation(summary = "Find publisher by id oeration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success publisher found"),
            @ApiResponse(responseCode = "404", description = "Publisher not found error")
    })
    PublisherDTO findById(Long id);

    @Operation(summary = "List all registered publishers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all registered publishers"),
            @ApiResponse(responseCode = "404", description = "Publisher not found error code")
    })
    List<PublisherDTO> findAll();

    @Operation(summary = "Delete publisher by Id operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success publisher deleted"),
            @ApiResponse(responseCode = "404", description = "Publisher not found error code")
    })
    void delete(@PathVariable Long id);
}
